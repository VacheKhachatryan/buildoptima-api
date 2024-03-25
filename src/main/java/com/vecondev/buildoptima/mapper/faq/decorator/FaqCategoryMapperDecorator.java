package com.vecondev.buildoptima.mapper.faq.decorator;

import com.vecondev.buildoptima.csv.faq.FaqCategoryRecord;
import com.vecondev.buildoptima.dto.EntityOverview;
import com.vecondev.buildoptima.dto.Metadata;
import com.vecondev.buildoptima.dto.faq.request.FaqCategoryRequestDto;
import com.vecondev.buildoptima.dto.faq.response.FaqCategoryResponseDto;
import com.vecondev.buildoptima.mapper.faq.FaqCategoryMapper;
import com.vecondev.buildoptima.mapper.user.UserMapper;
import com.vecondev.buildoptima.model.faq.FaqCategory;
import com.vecondev.buildoptima.model.user.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;

public abstract class FaqCategoryMapperDecorator implements FaqCategoryMapper {

  @Autowired
  @Qualifier("delegate")
  private FaqCategoryMapper faqCategoryMapper;

  @Autowired private UserMapper userMapper;

  @Override
  public FaqCategory mapToEntity(FaqCategoryRequestDto requestDto, User createdBy) {
    return faqCategoryMapper.mapToEntity(requestDto, createdBy).toBuilder()
        .createdBy(createdBy)
        .updatedBy(createdBy)
        .build();
  }

  @Override
  public FaqCategoryResponseDto mapToDto(FaqCategory faqCategory) {
    return faqCategoryMapper.mapToDto(faqCategory).toBuilder()
        .updatedBy(userMapper.mapToOverview(faqCategory.getUpdatedBy()))
        .id(faqCategory.getId())
        .build();
  }

  @Override
  public List<FaqCategoryResponseDto> mapToListDto(List<FaqCategory> categories) {
    return categories.stream().map(this::mapToDto).toList();
  }

  @Override
  public EntityOverview mapToOverview(FaqCategory faqCategory) {
    return faqCategoryMapper.mapToOverview(faqCategory);
  }

  @Override
  public List<FaqCategoryResponseDto> mapToListDtoFromPage(Page<FaqCategory> faqCategories) {
    return faqCategoryMapper.mapToListDtoFromPage(faqCategories);
  }

  @Override
  public FaqCategoryRecord mapToRecord(FaqCategory faqCategory) {
    return faqCategoryMapper.mapToRecord(faqCategory).toBuilder()
        .createdBy(faqCategory.getCreatedBy().getFullName())
        .updatedBy(faqCategory.getUpdatedBy().getFullName())
        .build();
  }

  @Override
  public Metadata getMetadata(FaqCategory category, Long allActiveCount, Long allArchivedCount) {
    return faqCategoryMapper.getMetadata(category, allActiveCount, allArchivedCount).toBuilder()
        .lastUpdatedAt(category.getUpdatedAt())
        .lastUpdatedBy(userMapper.mapToOverview(category.getUpdatedBy()))
        .build();
  }
}
