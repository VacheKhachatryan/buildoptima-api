package com.vecondev.buildoptima.mapper.faq.decorator;

import com.vecondev.buildoptima.csv.faq.FaqQuestionRecord;
import com.vecondev.buildoptima.dto.Metadata;
import com.vecondev.buildoptima.dto.faq.request.FaqQuestionRequestDto;
import com.vecondev.buildoptima.dto.faq.response.FaqQuestionResponseDto;
import com.vecondev.buildoptima.mapper.faq.FaqCategoryMapper;
import com.vecondev.buildoptima.mapper.faq.FaqQuestionMapper;
import com.vecondev.buildoptima.mapper.user.UserMapper;
import com.vecondev.buildoptima.model.faq.FaqCategory;
import com.vecondev.buildoptima.model.faq.FaqQuestion;
import com.vecondev.buildoptima.model.user.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;

public abstract class FaqQuestionMapperDecorator implements FaqQuestionMapper {

  @Autowired
  @Qualifier("delegate")
  private FaqQuestionMapper faqQuestionMapper;

  @Autowired private UserMapper userMapper;
  @Autowired private FaqCategoryMapper faqCategoryMapper;

  @Override
  public FaqQuestion mapToEntity(
      FaqQuestionRequestDto requestDto, FaqCategory faqCategory, User createdBy) {
    return faqQuestionMapper.mapToEntity(requestDto, faqCategory, createdBy).toBuilder()
        .createdBy(createdBy)
        .updatedBy(createdBy)
        .category(faqCategory)
        .build();
  }

  @Override
  public FaqQuestionResponseDto mapToDto(FaqQuestion question) {
    return faqQuestionMapper.mapToDto(question).toBuilder()
        .updatedBy(userMapper.mapToOverview(question.getUpdatedBy()))
        .category(faqCategoryMapper.mapToOverview(question.getCategory()))
        .build();
  }

  @Override
  public List<FaqQuestionResponseDto> mapToListDto(List<FaqQuestion> faqQuestions) {
    return faqQuestions.stream().map(this::mapToDto).toList();
  }

  @Override
  public List<FaqQuestionResponseDto> mapToListDtoFromPage(Page<FaqQuestion> faqQuestions) {
    return faqQuestionMapper.mapToListDtoFromPage(faqQuestions);
  }

  @Override
  public FaqQuestionRecord mapToRecord(FaqQuestion faqQuestion) {
    return faqQuestionMapper.mapToRecord(faqQuestion).toBuilder()
        .createdBy(faqQuestion.getCreatedBy().getFullName())
        .updatedBy(faqQuestion.getUpdatedBy().getFullName())
        .category(faqQuestion.getCategory().getName())
        .build();
  }

  @Override
  public Metadata getMetadata(FaqQuestion question, Long allActiveCount, Long allArchivedCount) {
    return faqQuestionMapper.getMetadata(question, allActiveCount, allArchivedCount).toBuilder()
        .lastUpdatedAt(question.getUpdatedAt())
        .lastUpdatedBy(userMapper.mapToOverview(question.getUpdatedBy()))
        .build();
  }
}
