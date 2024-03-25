package com.vecondev.buildoptima.mapper.faq;

import com.vecondev.buildoptima.csv.faq.FaqQuestionRecord;
import com.vecondev.buildoptima.dto.Metadata;
import com.vecondev.buildoptima.dto.faq.request.FaqQuestionRequestDto;
import com.vecondev.buildoptima.dto.faq.response.FaqQuestionResponseDto;
import com.vecondev.buildoptima.mapper.faq.decorator.FaqQuestionMapperDecorator;
import com.vecondev.buildoptima.model.faq.FaqCategory;
import com.vecondev.buildoptima.model.faq.FaqQuestion;
import com.vecondev.buildoptima.model.user.User;
import java.util.List;
import org.mapstruct.Context;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(FaqQuestionMapperDecorator.class)
public interface FaqQuestionMapper {

  FaqQuestion mapToEntity(
      FaqQuestionRequestDto requestDto, @Context FaqCategory faqCategory, @Context User createdBy);

  FaqQuestionResponseDto mapToDto(FaqQuestion question);

  List<FaqQuestionResponseDto> mapToListDto(List<FaqQuestion> faqQuestions);

  List<FaqQuestionResponseDto> mapToListDtoFromPage(Page<FaqQuestion> faqQuestions);

  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "category", ignore = true)
  FaqQuestionRecord mapToRecord(FaqQuestion faqQuestion);

  Metadata getMetadata(FaqQuestion question, Long allActiveCount, Long allArchivedCount);

  default List<FaqQuestionRecord> mapToRecordList(List<FaqQuestion> faqQuestions) {
    return faqQuestions.stream().map(this::mapToRecord).toList();
  }
}
