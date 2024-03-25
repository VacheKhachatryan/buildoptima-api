package com.vecondev.buildoptima.parameters.faq.question;

import static com.vecondev.buildoptima.exception.Error.FAQ_QUESTION_NOT_FOUND;
import static com.vecondev.buildoptima.filter.model.SearchOperation.GT;
import static com.vecondev.buildoptima.filter.model.SearchOperation.LIKE;
import static com.vecondev.buildoptima.model.Status.ACTIVE;
import static com.vecondev.buildoptima.model.Status.ARCHIVED;
import static com.vecondev.buildoptima.model.user.Role.MODERATOR;

import com.vecondev.buildoptima.dto.faq.request.FaqQuestionRequestDto;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.exception.FaqQuestionNotFoundException;
import com.vecondev.buildoptima.filter.model.Criteria;
import com.vecondev.buildoptima.filter.model.SortDto;
import com.vecondev.buildoptima.model.faq.FaqCategory;
import com.vecondev.buildoptima.model.faq.FaqQuestion;
import com.vecondev.buildoptima.model.user.User;
import com.vecondev.buildoptima.parameters.faq.category.FaqCategoryControllerTestParameters;
import com.vecondev.buildoptima.repository.faq.FaqCategoryRepository;
import com.vecondev.buildoptima.repository.user.UserRepository;
import java.util.List;
import java.util.Map;

public class FaqQuestionControllerTestParameters extends FaqQuestionTestParameters {

  private final FaqCategoryControllerTestParameters faqCategoryControllerTestParameters;
  private final UserRepository userRepository;
  private final FaqCategoryRepository faqCategoryRepository;

  public FaqQuestionControllerTestParameters(
      UserRepository userRepository, FaqCategoryRepository faqCategoryRepository) {
    this.faqCategoryControllerTestParameters =
        new FaqCategoryControllerTestParameters(userRepository);
    this.userRepository = userRepository;
    this.faqCategoryRepository = faqCategoryRepository;
  }

  public List<FaqQuestion> faqQuestions() {
    User moderator = userRepository.findByRole(MODERATOR).orElse(null);
    return List.of(
        new FaqQuestion(
            "Question1",
            "Answer1",
            ACTIVE,
            faqCategoryRepository.findAll().stream().findAny().orElse(null),
            moderator,
            moderator),
        new FaqQuestion(
            "Question2",
            "Answer2",
            ACTIVE,
            faqCategoryRepository.findAll().stream().findAny().orElse(null),
            moderator,
            moderator),
        new FaqQuestion(
            "Question3",
            "Answer3",
            ACTIVE,
            faqCategoryRepository.findAll().stream().findAny().orElse(null),
            moderator,
            moderator),
        new FaqQuestion(
            "Question4",
            "Answer4",
            ACTIVE,
            faqCategoryRepository.findAll().stream().findAny().orElse(null),
            moderator,
            moderator));
  }

  public List<FaqCategory> faqCategories() {
    return faqCategoryControllerTestParameters.faqCategories();
  }

  public List<User> users() {
    return faqCategoryControllerTestParameters.users();
  }

  public FaqQuestionRequestDto getFaqQuestionToSave() {
    return new FaqQuestionRequestDto(
        "Question_",
        "Answer_",
        ARCHIVED,
        faqCategoryRepository.findAll().stream()
            .findAny()
            .orElseThrow(() -> new FaqQuestionNotFoundException(FAQ_QUESTION_NOT_FOUND))
            .getId());
  }

  public FaqQuestionRequestDto getFaqQuestionWithDuplicatedQuestion() {
    return new FaqQuestionRequestDto(
        faqQuestions().stream()
            .findAny()
            .orElseThrow(() -> new FaqQuestionNotFoundException(FAQ_QUESTION_NOT_FOUND))
            .getQuestion(),
        "Answer?",
        ACTIVE,
        faqCategoryRepository.findAll().stream()
            .findAny()
            .orElseThrow(() -> new FaqQuestionNotFoundException(FAQ_QUESTION_NOT_FOUND))
            .getId());
  }

  public FetchRequestDto getInvalidFetchRequest() {
    return new FetchRequestDto(
        Map.of(
            "and",
            List.of(
                new Criteria(LIKE, "question", "how"),
                Map.of(
                    "or",
                    List.of(
                        new Criteria(LIKE, "answer", "password"),
                        new Criteria(GT, "creationDate", "2018-11-30T18:35:24.00Z"))))),
        0,
        10,
        List.of(new SortDto("question", SortDto.Direction.ASC)));
  }
}
