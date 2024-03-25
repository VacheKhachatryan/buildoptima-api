package com.vecondev.buildoptima.service.news;

import static com.vecondev.buildoptima.exception.Error.FAILED_JSON_CONVERTING;
import static com.vecondev.buildoptima.exception.Error.NEWS_ITEM_NOT_FOUND;
import static com.vecondev.buildoptima.filter.model.NewsFields.newsPageSortingFieldsMap;
import static com.vecondev.buildoptima.model.Status.ACTIVE;
import static com.vecondev.buildoptima.validation.validator.FieldNameValidator.validateFieldNames;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vecondev.buildoptima.csv.news.NewsRecord;
import com.vecondev.buildoptima.dto.EntityOverview;
import com.vecondev.buildoptima.dto.Metadata;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.filter.FetchResponseDto;
import com.vecondev.buildoptima.dto.news.NewsReport;
import com.vecondev.buildoptima.dto.news.request.NewsCreateRequestDto;
import com.vecondev.buildoptima.dto.news.request.NewsUpdateRequestDto;
import com.vecondev.buildoptima.dto.news.response.NewsResponseDto;
import com.vecondev.buildoptima.exception.FailedFileOperationException;
import com.vecondev.buildoptima.exception.NewsException;
import com.vecondev.buildoptima.filter.converter.PageableConverter;
import com.vecondev.buildoptima.filter.model.SortDto;
import com.vecondev.buildoptima.filter.specification.GenericSpecification;
import com.vecondev.buildoptima.mapper.news.NewsMapper;
import com.vecondev.buildoptima.mapper.user.UserMapper;
import com.vecondev.buildoptima.model.Status;
import com.vecondev.buildoptima.model.news.News;
import com.vecondev.buildoptima.model.news.NewsCategory;
import com.vecondev.buildoptima.model.user.User;
import com.vecondev.buildoptima.repository.news.NewsRepository;
import com.vecondev.buildoptima.repository.user.UserRepository;
import com.vecondev.buildoptima.security.user.AppUserDetails;
import com.vecondev.buildoptima.service.auth.SecurityContextService;
import com.vecondev.buildoptima.service.csv.CsvService;
import com.vecondev.buildoptima.service.s3.AmazonS3Service;
import com.vecondev.buildoptima.util.FileUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

  private static final String NEWS_JSON_FILE_NAME = "news.json";

  private final CsvService<NewsRecord> csvService;
  private final SecurityContextService securityContextService;
  private final AmazonS3Service amazonS3Service;

  private final NewsRepository newsRepository;
  private final UserRepository userRepository;

  private final NewsMapper newsMapper;
  private final UserMapper userMapper;
  private final ObjectMapper objectMapper;

  private final PageableConverter pageableConverter;

  @Override
  public NewsResponseDto create(NewsCreateRequestDto createNewsRequestDto) {
    UUID userId = securityContextService.getUserDetails().getId();
    log.info("Trying to add news item with title: {}", createNewsRequestDto.getTitle());
    User creator = userRepository.getReferenceById(userId);
    News news =
        newsMapper.mapToEntity(createNewsRequestDto, creator).toBuilder()
            .createdBy(creator.getId())
            .updatedBy(creator.getId())
            .build();
    log.info("Successfully saved news item in DB");

    if (createNewsRequestDto.getImage() != null) {
      amazonS3Service.uploadImagesToS3(
          news.getClass().getSimpleName().toLowerCase(),
          news.getId(),
          news.getImageVersion(),
          createNewsRequestDto.getImage(),
          userId);
      log.info("News image successfully uploaded to S3: news id {}", news.getId());
      news.setImageVersion(news.getImageVersion() + 1);
    }

    updateNewsInS3();
    log.info("Successfully saved news item in DB");
    return newsMapper.mapToResponseDto(newsRepository.saveAndFlush(news));
  }

  @Override
  public NewsResponseDto update(UUID newsId, NewsUpdateRequestDto newsRequestDto) {
    UUID userId = securityContextService.getUserDetails().getId();
    log.info("Trying to update news item: item id {}", newsId);
    News news =
        newsRepository.findById(newsId).orElseThrow(() -> new NewsException(NEWS_ITEM_NOT_FOUND));
    User user = userRepository.getReferenceById(userId);

    log.info("Trying to update news entity properties");
    updateNews(newsRequestDto, news, user, userId);
    log.info("Successfully updated news entity properties.");

    updateNewsInS3();
    return newsMapper.mapToResponseDto(news);
  }

  @Override
  public void delete(UUID id) {
    log.info("Trying to delete news item: news id {}", id);
    News news =
        newsRepository.findById(id).orElseThrow(() -> new NewsException(NEWS_ITEM_NOT_FOUND));
    String className = news.getClass().getSimpleName().toLowerCase();
    amazonS3Service.deleteImagesFromS3(className, id, news.getImageVersion());
    newsRepository.deleteById(id);
    log.info("Successfully deleted the news item: news id was {}", id);

    updateNewsInS3();
  }

  @Override
  public NewsResponseDto getById(UUID id) {
    String username = securityContextService.getUserDetails().getUsername();
    log.info("User {} is trying to get news item by id: {}", username, id);
    return newsMapper.mapToResponseDto(
        newsRepository.findById(id).orElseThrow(() -> new NewsException(NEWS_ITEM_NOT_FOUND)));
  }

  @Override
  public FetchResponseDto fetch(FetchRequestDto fetchRequest) {
    String username = securityContextService.getUserDetails().getUsername();
    log.info("User {} is trying to fetch news", username);
    validateFieldNames(newsPageSortingFieldsMap, fetchRequest.getSort());
    if (fetchRequest.getSort() == null || fetchRequest.getSort().isEmpty()) {
      SortDto sortDto = new SortDto("createdAt", SortDto.Direction.DESC);
      fetchRequest.setSort(List.of(sortDto));
    }
    Pageable pageable = pageableConverter.convert(fetchRequest);
    Specification<News> specification =
        new GenericSpecification<>(newsPageSortingFieldsMap, fetchRequest.getFilter());

    assert pageable != null;
    Page<News> result = newsRepository.findAll(specification, pageable);

    List<NewsResponseDto> content = newsMapper.mapToResponseList(result);
    log.info("Response was sent. {} results where found", content.size());
    return FetchResponseDto.builder()
        .content(content)
        .page(result.getNumber())
        .size(result.getSize())
        .totalElements(result.getTotalElements())
        .last(result.isLast())
        .build();
  }

  @Override
  public Metadata getMetadata() {
    String username = securityContextService.getUserDetails().getUsername();
    log.info("User {} is trying to get the news metadata", username);
    News lastUpdated = newsRepository.findTopByOrderByUpdatedAtDesc();
    long allActiveCount = newsRepository.countByStatus(ACTIVE);
    long allArchivedCount = newsRepository.countByStatus(Status.ARCHIVED);

    if (lastUpdated == null) {
      return new Metadata();
    }
    EntityOverview lastModifier =
        userMapper.mapToOverview(
            userRepository.getReferenceById(
                lastUpdated.getUpdatedBy() != null
                    ? lastUpdated.getUpdatedBy()
                    : lastUpdated.getCreatedBy()));

    log.info("User {} successfully got the news metadata", username);
    return Metadata.builder()
        .lastUpdatedAt(lastUpdated.getUpdatedAt())
        .lastUpdatedBy(lastModifier)
        .allActiveCount(allActiveCount)
        .allArchivedCount(allArchivedCount)
        .build();
  }

  @Override
  public InputStreamResource exportCsv(FetchRequestDto fetchRequestDto) {
    String username = securityContextService.getUserDetails().getUsername();
    log.info("User {} is trying to export news in csv file", username);
    Specification<News> specification =
        new GenericSpecification<>(newsPageSortingFieldsMap, fetchRequestDto.getFilter());

    List<News> news = newsRepository.findAll(specification);
    List<NewsRecord> newsDtoList = newsMapper.mapToNewsRecordList(news);

    ByteArrayInputStream inputStream = csvService.writeToCsv(newsDtoList, NewsRecord.class);
    log.info(
        "User {} successfully exported {} news items in csv file", username, newsDtoList.size());
    return new InputStreamResource(inputStream);
  }

  @Override
  public NewsResponseDto archiveNews(UUID id) {
    AppUserDetails userDetails = securityContextService.getUserDetails();
    log.info("User {} is trying to archive news item with id {}", userDetails.getUsername(), id);
    News news =
        newsRepository.findById(id).orElseThrow(() -> new NewsException(NEWS_ITEM_NOT_FOUND));
    news.setStatus(Status.ARCHIVED);
    news.setUpdatedBy(userDetails.getId());
    NewsResponseDto newsResponseDto = newsMapper.mapToResponseDto(news);
    log.info("User {} successfully archived news item with id {}", userDetails.getUsername(), id);

    updateNewsInS3();
    return newsResponseDto;
  }

  private void updateNews(NewsUpdateRequestDto dto, News news, User modifier, UUID userId) {
    if (dto.getTitle() != null) {
      news.setTitle(dto.getTitle());
    }
    if (dto.getSummary() != null) {
      news.setSummary(dto.getSummary());
    }
    if (dto.getDescription() != null) {
      news.setDescription(dto.getDescription());
    }
    if (dto.getCategory() != null) {
      news.setCategory(NewsCategory.valueOf(dto.getCategory()));
    }
    if (dto.getImage() != null) {
      String className = news.getClass().getSimpleName().toLowerCase();
      amazonS3Service.uploadImagesToS3(
          className, news.getId(), news.getImageVersion(), dto.getImage(), userId);
      news.setImageVersion(news.getImageVersion() + 1);
    }
    news.setUpdatedBy(modifier.getId());
  }

  private void updateNewsInS3() {
    File file = new File(NEWS_JSON_FILE_NAME);
    try (FileWriter fileWriter = new FileWriter(file)) {
      fileWriter.write(
          objectMapper.writeValueAsString(
              new NewsReport(
                  newsRepository.findAllByStatus(ACTIVE).stream()
                      .map(newsMapper::mapToResponseDto)
                      .toList())));

    } catch (IOException ex) {
      log.warn("""
               Exception occurred while constructing news report
               .json file to update in s3 bucket.""");
      throw new FailedFileOperationException(FAILED_JSON_CONVERTING);
    }

    amazonS3Service.uploadJsonObject(NEWS_JSON_FILE_NAME, file);
    FileUtil.deleteFile(file);
    log.info("News report file is successfully updated in s3 bucket.");
  }
}
