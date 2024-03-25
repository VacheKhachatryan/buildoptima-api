package com.vecondev.buildoptima.mapper.faq;

import com.vecondev.buildoptima.csv.faq.FaqCategoryRecord;
import com.vecondev.buildoptima.dto.EntityOverview;
import com.vecondev.buildoptima.dto.Metadata;
import com.vecondev.buildoptima.dto.faq.request.FaqCategoryRequestDto;
import com.vecondev.buildoptima.dto.faq.response.FaqCategoryResponseDto;
import com.vecondev.buildoptima.mapper.faq.decorator.FaqCategoryMapperDecorator;
import com.vecondev.buildoptima.model.faq.FaqCategory;
import com.vecondev.buildoptima.model.user.User;
import java.util.List;
import org.mapstruct.Context;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(FaqCategoryMapperDecorator.class)
public interface FaqCategoryMapper {

  FaqCategory mapToEntity(FaqCategoryRequestDto requestDto, @Context User createdBy);

  @Mapping(target = "updatedBy", ignore = true)
  FaqCategoryResponseDto mapToDto(FaqCategory faqCategory);

  EntityOverview mapToOverview(FaqCategory faqCategory);

  List<FaqCategoryResponseDto> mapToListDto(List<FaqCategory> categories);

  List<FaqCategoryResponseDto> mapToListDtoFromPage(Page<FaqCategory> categories);

  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  FaqCategoryRecord mapToRecord(FaqCategory faqCategory);

  Metadata getMetadata(FaqCategory category, Long allActiveCount, Long allArchivedCount);

  default List<FaqCategoryRecord> mapToRecordList(List<FaqCategory> faqCategories) {
    return faqCategories.stream().map(this::mapToRecord).toList();
  }
}
