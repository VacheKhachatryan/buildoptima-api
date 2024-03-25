package com.vecondev.buildoptima.mapper.news;

import com.vecondev.buildoptima.csv.news.NewsRecord;
import com.vecondev.buildoptima.dto.news.request.NewsCreateRequestDto;
import com.vecondev.buildoptima.dto.news.response.NewsResponseDto;
import com.vecondev.buildoptima.mapper.user.UserMapper;
import com.vecondev.buildoptima.model.Status;
import com.vecondev.buildoptima.model.news.News;
import com.vecondev.buildoptima.model.news.NewsCategory;
import com.vecondev.buildoptima.model.user.User;
import com.vecondev.buildoptima.repository.user.UserRepository;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;

public abstract class NewsMapperDecorator implements NewsMapper {

  @Autowired
  @Qualifier("delegate")
  private NewsMapper mapper;

  @Autowired private UserMapper userMapper;
  @Autowired private UserRepository userRepository;

  @Override
  public News mapToEntity(NewsCreateRequestDto dto, User createdBy) {
    News news =
        mapper.mapToEntity(dto, createdBy).toBuilder()
            .category(NewsCategory.valueOf(dto.getCategory()))
            .status(Status.ACTIVE)
            .createdBy(createdBy.getId())
            .updatedBy(createdBy.getId())
            .imageVersion(0)
            .build();
    StringBuilder keywords = new StringBuilder();
    dto.getKeywords().forEach(keyword -> keywords.append(keyword).append(" "));
    news.setKeywords(keywords.toString());
    return news;
  }

  @Override
  public NewsResponseDto mapToResponseDto(News news) {
    NewsResponseDto responseDto = mapper.mapToResponseDto(news);
    String keywordsAsString = news.getKeywords();
    if (keywordsAsString != null) {
      List<String> keywords = Arrays.stream(keywordsAsString.split(" ")).toList();
      responseDto.setKeywords(keywords);
    }
    responseDto.setCreatedBy(
        userMapper.mapToOverview(userRepository.getReferenceById(news.getCreatedBy())));
    responseDto.setUpdatedBy(
        userMapper.mapToOverview(userRepository.getReferenceById(news.getUpdatedBy())));
    return responseDto;
  }

  @Override
  public NewsRecord mapToRecord(News news) {
    NewsRecord newsRecord = mapper.mapToRecord(news);
    User creator = userRepository.getReferenceById(news.getCreatedBy());
    newsRecord.setCreatedBy(String.format("%s %s", creator.getFirstName(), creator.getLastName()));
    User modifier = userRepository.getReferenceById(news.getUpdatedBy());
    newsRecord.setUpdatedBy(
        String.format("%s %s", modifier.getFirstName(), modifier.getLastName()));
    return newsRecord;
  }

  @Override
  public List<NewsResponseDto> mapToResponseList(Page<News> newsPage) {
    return newsPage.getContent().stream().map(this::mapToResponseDto).toList();
  }

  @Override
  public List<NewsRecord> mapToNewsRecordList(List<News> news) {
    return news.stream().map(this::mapToRecord).toList();
  }
}
