package com.vecondev.buildoptima.parameters.faq.question;

import static com.vecondev.buildoptima.model.Status.ACTIVE;

import com.vecondev.buildoptima.csv.faq.FaqQuestionRecord;
import com.vecondev.buildoptima.dto.EntityOverview;
import com.vecondev.buildoptima.dto.faq.request.FaqQuestionRequestDto;
import com.vecondev.buildoptima.dto.faq.response.FaqQuestionResponseDto;
import com.vecondev.buildoptima.model.faq.FaqCategory;
import com.vecondev.buildoptima.model.faq.FaqQuestion;
import com.vecondev.buildoptima.model.user.User;
import com.vecondev.buildoptima.parameters.PageableTest;
import com.vecondev.buildoptima.parameters.faq.category.FaqCategoryServiceTestParameters;
import com.vecondev.buildoptima.parameters.user.UserServiceTestParameters;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FaqQuestionServiceTestParameters extends FaqQuestionTestParameters
    implements PageableTest {

  private final UserServiceTestParameters userServiceTestParameters =
      new UserServiceTestParameters();
  private final FaqCategoryServiceTestParameters faqCategoryServiceTestParameters =
      new FaqCategoryServiceTestParameters();

  public FaqQuestionRequestDto getFaqQuestionRequestDto() {
    return new FaqQuestionRequestDto(
        "Question", "Answer", ACTIVE, UUID.fromString("57ebd52d-6924-4b33-9e48-ce2c68eb9f28"));
  }

  public FaqQuestion getFaqQuestion(UUID userId) {
    FaqQuestionRequestDto faqQuestionRequestDto = getFaqQuestionRequestDto();
    User user = getUserById(userId);
    FaqQuestion faqQuestion =
        new FaqQuestion(
            faqQuestionRequestDto.getQuestion(),
            faqQuestionRequestDto.getAnswer(),
            faqQuestionRequestDto.getStatus(),
            faqCategoryServiceTestParameters.getFaqCategory(userId),
            user,
            user);
    faqQuestion.setId(UUID.fromString("c12bca5d-1ae8-4043-a66a-b55ce799df4c"));

    return faqQuestion;
  }

  public FaqQuestionResponseDto getFaqQuestionResponseDto(FaqQuestion question) {
    User user = question.getUpdatedBy();
    return new FaqQuestionResponseDto(
        question.getId(),
        question.getQuestion(),
        question.getAnswer(),
        question.getStatus(),
        new EntityOverview(question.getCategory().getId(), question.getCategory().getName()),
        new EntityOverview(
            user.getId(), String.format("%s %s", user.getFirstName(), user.getLastName())),
        question.getCreatedAt(),
        question.getUpdatedAt());
  }

  public List<FaqQuestion> getFaqQuestionList() {
    return List.of(
        getFaqQuestion(UUID.randomUUID()),
        getFaqQuestion(UUID.randomUUID()).toBuilder()
            .question("Question_")
            .answer("Answer_")
            .build());
  }

  public List<FaqQuestionResponseDto> getFaqQuestionResponseDtoList(
      List<FaqQuestion> faqQuestions) {
    return faqQuestions.stream().map(this::getFaqQuestionResponseDto).collect(Collectors.toList());
  }

  public List<FaqQuestionRecord> getFaqQuestionRecordList() {
    String createdBy = String.format("John Smith (%s)", UUID.randomUUID());
    String category = "Properties";
    return List.of(
        new FaqQuestionRecord(
            UUID.randomUUID(),
            "Question1",
            "Answer1",
            ACTIVE,
            category,
            createdBy,
            Instant.now(),
            createdBy,
            Instant.now()),
        new FaqQuestionRecord(
            UUID.randomUUID(),
            "Question2",
            "Answer2",
            ACTIVE,
            category,
            createdBy,
            Instant.now(),
            createdBy,
            Instant.now()));
  }

  public FaqCategory getFaqCategory(UUID userId) {
    return faqCategoryServiceTestParameters.getFaqCategory(userId);
  }

  public List<FaqCategory> getFaqCategories() {
    return faqCategoryServiceTestParameters.getFaqCategoryList();
  }

  public User getUserById(UUID userId) {
    User user = userServiceTestParameters.getSavedUser();
    user.setId(userId);

    return user;
  }

  public List<User> getUsers() {
    return userServiceTestParameters.getUserList();
  }
}
