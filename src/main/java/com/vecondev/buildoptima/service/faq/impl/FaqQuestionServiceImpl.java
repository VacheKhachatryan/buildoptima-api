package com.vecondev.buildoptima.service.faq.impl;

import static com.vecondev.buildoptima.exception.Error.FAQ_QUESTION_NOT_FOUND;
import static com.vecondev.buildoptima.exception.Error.INVALID_FIELD;
import static com.vecondev.buildoptima.filter.model.DictionaryField.CATEGORY;
import static com.vecondev.buildoptima.filter.model.DictionaryField.UPDATED_BY;
import static com.vecondev.buildoptima.filter.model.FaqQuestionFields.faqQuestionPageSortingFieldsMap;
import static com.vecondev.buildoptima.model.Status.ACTIVE;
import static com.vecondev.buildoptima.model.Status.ARCHIVED;
import static com.vecondev.buildoptima.validation.validator.FieldNameValidator.validateFieldNames;

import com.vecondev.buildoptima.csv.faq.FaqQuestionRecord;
import com.vecondev.buildoptima.dto.EntityOverview;
import com.vecondev.buildoptima.dto.Metadata;
import com.vecondev.buildoptima.dto.faq.request.FaqQuestionRequestDto;
import com.vecondev.buildoptima.dto.faq.response.FaqQuestionResponseDto;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.filter.FetchResponseDto;
import com.vecondev.buildoptima.exception.FaqQuestionNotFoundException;
import com.vecondev.buildoptima.exception.InvalidFieldException;
import com.vecondev.buildoptima.filter.converter.PageableConverter;
import com.vecondev.buildoptima.filter.model.DictionaryField;
import com.vecondev.buildoptima.filter.model.SortDto;
import com.vecondev.buildoptima.filter.specification.GenericSpecification;
import com.vecondev.buildoptima.mapper.faq.FaqQuestionMapper;
import com.vecondev.buildoptima.model.Status;
import com.vecondev.buildoptima.model.faq.FaqCategory;
import com.vecondev.buildoptima.model.faq.FaqQuestion;
import com.vecondev.buildoptima.model.user.User;
import com.vecondev.buildoptima.repository.faq.FaqQuestionRepository;
import com.vecondev.buildoptima.service.csv.CsvService;
import com.vecondev.buildoptima.service.faq.FaqCategoryService;
import com.vecondev.buildoptima.service.faq.FaqQuestionService;
import com.vecondev.buildoptima.service.user.UserService;
import com.vecondev.buildoptima.validation.faq.FaqQuestionValidator;
import java.util.ArrayList;
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
public class FaqQuestionServiceImpl implements FaqQuestionService {

  private final FaqQuestionMapper faqQuestionMapper;
  private final FaqQuestionValidator faqQuestionValidator;
  private final FaqQuestionRepository faqQuestionRepository;

  private final FaqCategoryService faqCategoryService;
  private final UserService userService;
  private final PageableConverter pageableConverter;
  private final CsvService<FaqQuestionRecord> csvService;

  @Override
  public List<FaqQuestionResponseDto> getAll() {
    List<FaqQuestion> faqQuestions = faqQuestionRepository.findAll();

    return faqQuestionMapper.mapToListDto(faqQuestions);
  }

  @Override
  public FaqQuestionResponseDto getById(UUID questionId) {
    return faqQuestionMapper.mapToDto(findQuestionById(questionId));
  }

  @Override
  public FaqQuestionResponseDto create(FaqQuestionRequestDto requestDto, UUID userId) {
    User user = userService.findUserById(userId);
    FaqCategory faqCategory = faqCategoryService.findCategoryById(requestDto.getFaqCategoryId());
    FaqQuestion faqQuestion = faqQuestionMapper.mapToEntity(requestDto, faqCategory, user);
    faqQuestionValidator.validateQuestion(faqQuestion.getQuestion());

    faqQuestion = faqQuestionRepository.saveAndFlush(faqQuestion);
    log.info("User with id: {} created new FAQ QUESTION with id: {}", userId, faqQuestion.getId());

    return faqQuestionMapper.mapToDto(faqQuestion);
  }

  @Override
  public FaqQuestionResponseDto update(
      UUID questionId, FaqQuestionRequestDto requestDto, UUID userId) {
    FaqQuestion question =
        faqQuestionRepository
            .findById(questionId)
            .orElseThrow(() -> new FaqQuestionNotFoundException(FAQ_QUESTION_NOT_FOUND));
    faqQuestionValidator.validateQuestion(requestDto.getQuestion());

    question =
        question.toBuilder()
            .question(requestDto.getQuestion())
            .answer(requestDto.getAnswer())
            .status(requestDto.getStatus())
            .category(faqCategoryService.findCategoryById(requestDto.getFaqCategoryId()))
            .updatedBy(userService.findUserById(userId))
            .build();
    log.info("User with id: {} updated the FAQ Question with id: {}", userId, questionId);

    return faqQuestionMapper.mapToDto(faqQuestionRepository.save(question));
  }

  @Override
  public void delete(UUID questionId, UUID userId) {
    if (!faqQuestionRepository.existsById(questionId)) {
      throw new FaqQuestionNotFoundException(FAQ_QUESTION_NOT_FOUND);
    }

    faqQuestionRepository.deleteById(questionId);
    log.info("User with id: {} deleted the FAQ Question with id: {}", userId, questionId);
  }

  @Override
  public FaqQuestion findQuestionById(UUID questionId) {
    return faqQuestionRepository
        .findById(questionId)
        .orElseThrow(() -> new FaqQuestionNotFoundException(FAQ_QUESTION_NOT_FOUND));
  }

  @Override
  public FetchResponseDto fetch(FetchRequestDto fetchRequest) {
    log.info("Request to fetch FAQ questions from DB");
    validateFieldNames(faqQuestionPageSortingFieldsMap, fetchRequest.getSort());
    if (fetchRequest.getSort() == null || fetchRequest.getSort().isEmpty()) {
      SortDto sortDto = new SortDto("questions", SortDto.Direction.ASC);
      fetchRequest.setSort(List.of(sortDto));
    }
    Pageable pageable = pageableConverter.convert(fetchRequest);
    Specification<FaqQuestion> specification =
        new GenericSpecification<>(faqQuestionPageSortingFieldsMap, fetchRequest.getFilter());
    Page<FaqQuestion> result = faqQuestionRepository.findAll(specification, pageable);

    List<FaqQuestionResponseDto> content = faqQuestionMapper.mapToListDtoFromPage(result);
    log.info("Response was sent. {} results where found", content.size());
    return FetchResponseDto.builder()
        .content(content)
        .page(result.getNumber())
        .size(result.getSize())
        .totalElements(result.getTotalElements())
        .last(result.isLast())
        .build();
  }

  /** Exports all faq questions in csv file. */
  public ResponseEntity<Resource> exportInCsv() {
    List<FaqQuestion> questions = faqQuestionRepository.findAll();
    List<FaqQuestionRecord> questionRecords = faqQuestionMapper.mapToRecordList(questions);
    InputStreamResource questionsResource =
        new InputStreamResource(csvService.writeToCsv(questionRecords, FaqQuestionRecord.class));

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType("application/csv"))
        .header("Content-disposition", "attachment; filename=FaqQuestions.csv")
        .body(questionsResource);
  }

  @Override
  public Metadata getMetadata() {
    FaqQuestion question = faqQuestionRepository.findTopByOrderByUpdatedAtDesc().orElse(null);
    if (question == null) {
      return new Metadata();
    }

    return faqQuestionMapper.getMetadata(
        question,
        faqQuestionRepository.countByStatus(ACTIVE),
        faqQuestionRepository.countByStatus(ARCHIVED));
  }

  @Override
  public List<EntityOverview> lookup(Status status, DictionaryField dictionary) {
    List<EntityOverview> response = new ArrayList<>();
    if (dictionary == UPDATED_BY) {
      response.addAll(
          faqQuestionRepository.findDistinctModifiers(status).stream()
              .map(
                  user ->
                      new EntityOverview(
                          user.getId(),
                          String.format("%s %s", user.getFirstName(), user.getLastName())))
              .toList());
      log.info(
          "It's found {} results while looking up users who updated {} questions.",
          response.size(),
          status);
      return response;
    } else if (dictionary == CATEGORY) {
      response.addAll(
          faqQuestionRepository.findDistinctCategories(status).stream()
              .map(category -> new EntityOverview(category.getId(), category.getName()))
              .toList());
      log.info(
          "It's found {} results while looking up categories that have {} questions.",
          response.size(),
          status);
      return response;
    } else {
      throw new InvalidFieldException(INVALID_FIELD);
    }
  }
}
