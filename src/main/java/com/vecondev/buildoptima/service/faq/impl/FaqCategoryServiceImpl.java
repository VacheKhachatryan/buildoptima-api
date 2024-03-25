package com.vecondev.buildoptima.service.faq.impl;

import static com.vecondev.buildoptima.exception.Error.CATEGORY_HAS_QUESTIONS;
import static com.vecondev.buildoptima.exception.Error.FAQ_CATEGORY_NOT_FOUND;
import static com.vecondev.buildoptima.filter.model.FaqCategoryFields.faqCategoryPageSortingFieldsMap;
import static com.vecondev.buildoptima.validation.validator.FieldNameValidator.validateFieldNames;

import com.vecondev.buildoptima.csv.faq.FaqCategoryRecord;
import com.vecondev.buildoptima.dto.Metadata;
import com.vecondev.buildoptima.dto.faq.request.FaqCategoryRequestDto;
import com.vecondev.buildoptima.dto.faq.response.FaqCategoryResponseDto;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.filter.FetchResponseDto;
import com.vecondev.buildoptima.exception.DataIntegrityViolationException;
import com.vecondev.buildoptima.exception.FaqCategoryNotFoundException;
import com.vecondev.buildoptima.filter.converter.PageableConverter;
import com.vecondev.buildoptima.filter.model.SortDto;
import com.vecondev.buildoptima.filter.specification.GenericSpecification;
import com.vecondev.buildoptima.mapper.faq.FaqCategoryMapper;
import com.vecondev.buildoptima.model.faq.FaqCategory;
import com.vecondev.buildoptima.model.user.User;
import com.vecondev.buildoptima.repository.faq.FaqCategoryRepository;
import com.vecondev.buildoptima.service.csv.CsvService;
import com.vecondev.buildoptima.service.faq.FaqCategoryService;
import com.vecondev.buildoptima.service.user.UserService;
import com.vecondev.buildoptima.validation.faq.FaqCategoryValidator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FaqCategoryServiceImpl implements FaqCategoryService {

  private final FaqCategoryMapper faqCategoryMapper;
  private final FaqCategoryRepository faqCategoryRepository;
  private final FaqCategoryValidator faqCategoryValidator;
  private final PageableConverter pageableConverter;
  private final CsvService<FaqCategoryRecord> csvService;

  private final UserService userService;

  @Override
  public List<FaqCategoryResponseDto> getAll() {
    List<FaqCategory> faqCategories = faqCategoryRepository.findAll();

    return faqCategoryMapper.mapToListDto(faqCategories);
  }

  @Override
  public FaqCategoryResponseDto getById(UUID categoryId) {
    return faqCategoryMapper.mapToDto(findCategoryById(categoryId));
  }

  @Override
  public FaqCategoryResponseDto create(FaqCategoryRequestDto requestDto, UUID userId) {
    User user = userService.findUserById(userId);
    FaqCategory faqCategory = faqCategoryMapper.mapToEntity(requestDto, user);
    faqCategoryValidator.validateCategoryName(faqCategory.getName());

    faqCategory = faqCategoryRepository.saveAndFlush(faqCategory);
    log.info(
        "User with id: {} created new FAQ Category with name: {}", userId, requestDto.getName());

    return faqCategoryMapper.mapToDto(faqCategory);
  }

  @Override
  public FaqCategoryResponseDto update(
      UUID categoryId, FaqCategoryRequestDto requestDto, UUID userId) {
    FaqCategory category =
        faqCategoryRepository
            .findById(categoryId)
            .orElseThrow(() -> new FaqCategoryNotFoundException(FAQ_CATEGORY_NOT_FOUND));
    faqCategoryValidator.validateCategoryName(requestDto.getName());

    category =
        category.toBuilder()
            .name(requestDto.getName())
            .createdBy(userService.findUserById(userId))
            .build();
    log.info("User with id: {} updated the FAQ Category with id: {}", userId, categoryId);
    return faqCategoryMapper.mapToDto(faqCategoryRepository.saveAndFlush(category));
  }

  @Override
  public void delete(UUID categoryId, UUID userId) {
    if (!faqCategoryRepository.existsById(categoryId)) {
      log.warn(
          "User with id: {} wants to delete FAQ category with id: {} that doesn't exist.",
          userId,
          categoryId);
      throw new FaqCategoryNotFoundException(FAQ_CATEGORY_NOT_FOUND);
    }
    if (Boolean.TRUE.equals(faqCategoryRepository.hasAnyQuestion(categoryId))) {
      log.warn(
          "User with id: {} wants to delete FAQ category with id: {} that has dependent questions.",
          userId,
          categoryId);
      throw new DataIntegrityViolationException(CATEGORY_HAS_QUESTIONS);
    }

    faqCategoryRepository.deleteById(categoryId);
    log.info("User with id: {} deleted the FAQ Category with id: {}", userId, categoryId);
  }

  @Override
  public FaqCategory findCategoryById(UUID id) {
    return faqCategoryRepository
        .findById(id)
        .orElseThrow(() -> new FaqCategoryNotFoundException(FAQ_CATEGORY_NOT_FOUND));
  }

  @Override
  public FetchResponseDto fetch(FetchRequestDto fetchRequest) {
    log.info("Request to fetch users from DB");
    validateFieldNames(faqCategoryPageSortingFieldsMap, fetchRequest.getSort());
    if (fetchRequest.getSort() == null || fetchRequest.getSort().isEmpty()) {
      SortDto sortDto = new SortDto("name", SortDto.Direction.ASC);
      fetchRequest.setSort(List.of(sortDto));
    }
    Pageable pageable = pageableConverter.convert(fetchRequest);
    Specification<FaqCategory> specification =
        new GenericSpecification<>(faqCategoryPageSortingFieldsMap, fetchRequest.getFilter());
    Page<FaqCategory> result = faqCategoryRepository.findAll(specification, pageable);

    List<FaqCategoryResponseDto> content = faqCategoryMapper.mapToListDtoFromPage(result);
    log.info("Response was sent. {} results where found", content.size());
    return FetchResponseDto.builder()
        .content(content)
        .page(result.getNumber())
        .size(result.getSize())
        .totalElements(result.getTotalElements())
        .last(result.isLast())
        .build();
  }

  /** Exports all faq categories in csv file. */
  @Override
  public ResponseEntity<Resource> exportInCsv() {
    List<FaqCategory> categories = faqCategoryRepository.findAll();
    List<FaqCategoryRecord> categoryRecords = faqCategoryMapper.mapToRecordList(categories);
    InputStreamResource categoriesResource =
        new InputStreamResource(csvService.writeToCsv(categoryRecords, FaqCategoryRecord.class));

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType("application/csv"))
        .header("Content-disposition", "attachment; filename=FaqCategories.csv")
        .body(categoriesResource);
  }

  @Override
  public Metadata getMetadata() {
    FaqCategory category = faqCategoryRepository.findTopByOrderByUpdatedAtDesc().orElse(null);
    if (category == null) {
      return new Metadata();
    }

    return faqCategoryMapper.getMetadata(category, faqCategoryRepository.count(), 0L);
  }
}
