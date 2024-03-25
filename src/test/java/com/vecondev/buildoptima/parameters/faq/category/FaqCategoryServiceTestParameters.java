package com.vecondev.buildoptima.parameters.faq.category;

import com.vecondev.buildoptima.csv.faq.FaqCategoryRecord;
import com.vecondev.buildoptima.dto.EntityOverview;
import com.vecondev.buildoptima.dto.faq.request.FaqCategoryRequestDto;
import com.vecondev.buildoptima.dto.faq.response.FaqCategoryResponseDto;
import com.vecondev.buildoptima.model.faq.FaqCategory;
import com.vecondev.buildoptima.model.user.User;
import com.vecondev.buildoptima.parameters.PageableTest;
import com.vecondev.buildoptima.parameters.user.UserServiceTestParameters;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FaqCategoryServiceTestParameters extends FaqCategoryTestParameters
    implements PageableTest {

  private final UserServiceTestParameters userServiceTestParameters =
      new UserServiceTestParameters();

  public FaqCategoryRequestDto getFaqCategoryRequestDto() {
    return new FaqCategoryRequestDto("Royalties");
  }

  public FaqCategory getFaqCategory(UUID userId) {
    User user = getUserById(userId);
    FaqCategory faqCategory = new FaqCategory(getFaqCategoryRequestDto().getName(), user, user);
    faqCategory.setId(UUID.fromString("57ebd52d-6924-4b33-9e48-ce2c68eb9f28"));

    return faqCategory;
  }

  public FaqCategoryResponseDto getFaqCategoryResponseDto(FaqCategory category) {
    return new FaqCategoryResponseDto(
        category.getId(),
        category.getName(),
        new EntityOverview(
            category.getUpdatedBy().getId(),
            String.format(
                "%s %s",
                category.getUpdatedBy().getFirstName(), category.getUpdatedBy().getLastName())),
        category.getCreatedAt(),
        category.getUpdatedAt());
  }

  public List<FaqCategory> getFaqCategoryList() {
    return List.of(
        getFaqCategory(UUID.randomUUID()),
        getFaqCategory(UUID.randomUUID()).toBuilder().name("Properties").build());
  }

  public List<FaqCategoryRecord> getFaqCategoryRecordList() {
    String createdBy = String.format("John Smith (%s)", UUID.randomUUID());
    return List.of(
        new FaqCategoryRecord(
            UUID.randomUUID(), "Royalties", createdBy, Instant.now(), createdBy, Instant.now()),
        new FaqCategoryRecord(
            UUID.randomUUID(), "Properties", createdBy, Instant.now(), createdBy, Instant.now()));
  }

  public List<FaqCategoryResponseDto> getFaqCategoryResponseDtoList(List<FaqCategory> categories) {
    return categories.stream().map(this::getFaqCategoryResponseDto).collect(Collectors.toList());
  }

  public User getUserById(UUID userId) {
    User user = userServiceTestParameters.getSavedUser();
    user.setId(userId);

    return user;
  }
}
