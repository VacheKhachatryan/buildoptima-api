package com.vecondev.buildoptima.parameters.faq.category;

import static com.vecondev.buildoptima.exception.Error.FAQ_CATEGORY_NOT_FOUND;
import static com.vecondev.buildoptima.filter.model.SearchOperation.EQ;
import static com.vecondev.buildoptima.filter.model.SearchOperation.GT;
import static com.vecondev.buildoptima.model.user.Role.CLIENT;
import static com.vecondev.buildoptima.model.user.Role.MODERATOR;

import com.vecondev.buildoptima.dto.faq.request.FaqCategoryRequestDto;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.exception.FaqCategoryNotFoundException;
import com.vecondev.buildoptima.filter.model.Criteria;
import com.vecondev.buildoptima.filter.model.SortDto;
import com.vecondev.buildoptima.model.faq.FaqCategory;
import com.vecondev.buildoptima.model.user.User;
import com.vecondev.buildoptima.repository.user.UserRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FaqCategoryControllerTestParameters extends FaqCategoryTestParameters {

  private final UserRepository userRepository;

  public List<User> users() {
    return List.of(
        new User("John", "Smith", "+712345678", "john@mail.ru", "John1234.", MODERATOR, true, 0),
        new User("John", "Stone", "+612345678", "john@gmail.com", "John1234/", CLIENT, true, 0));
  }

  public List<FaqCategory> faqCategories() {
    return List.of(
        new FaqCategory(
            "Royalties",
            userRepository.findAll().stream()
                .filter(user -> user.getRole() == MODERATOR)
                .findFirst()
                .orElse(null),
            userRepository.findAll().stream()
                .filter(user -> user.getRole() == MODERATOR)
                .findFirst()
                .orElse(null)),
        new FaqCategory(
            "Registration",
            userRepository.findAll().stream()
                .filter(user -> user.getRole() == MODERATOR)
                .findFirst()
                .orElse(null),
            userRepository.findAll().stream()
                .filter(user -> user.getRole() == MODERATOR)
                .findFirst()
                .orElse(null)));
  }

  public FaqCategoryRequestDto getFaqCategoryToSave() {
    return new FaqCategoryRequestDto("Password recovery");
  }

  public FaqCategoryRequestDto getFaqCategoryWithDuplicatedNameToSave() {
    return new FaqCategoryRequestDto(
        faqCategories().stream()
            .findAny()
            .orElseThrow(() -> new FaqCategoryNotFoundException(FAQ_CATEGORY_NOT_FOUND))
            .getName());
  }

  public FetchRequestDto getInvalidFetchRequest() {
    return new FetchRequestDto(
        Map.of(
            "and",
            List.of(
                new Criteria(EQ, "title", "Royalties"),
                Map.of(
                    "or",
                    List.of(
                        new Criteria(GT, "created", "2018-11-30T18:35:24.00Z"),
                        new Criteria(GT, "updatedAt", "2018-11-30T18:35:24.00Z"))))),
        0,
        10,
        List.of(new SortDto("name", SortDto.Direction.ASC)));
  }
}
