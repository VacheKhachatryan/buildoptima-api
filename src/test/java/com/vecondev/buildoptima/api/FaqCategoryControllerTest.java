package com.vecondev.buildoptima.api;

import static com.vecondev.buildoptima.model.user.Role.CLIENT;
import static com.vecondev.buildoptima.model.user.Role.MODERATOR;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vecondev.buildoptima.actions.FaqCategoryResultActions;
import com.vecondev.buildoptima.dto.faq.request.FaqCategoryRequestDto;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.endpoints.FaqCategoryEndpointUris;
import com.vecondev.buildoptima.model.faq.FaqCategory;
import com.vecondev.buildoptima.model.user.Role;
import com.vecondev.buildoptima.model.user.User;
import com.vecondev.buildoptima.parameters.faq.category.FaqCategoryControllerTestParameters;
import com.vecondev.buildoptima.repository.faq.FaqCategoryRepository;
import com.vecondev.buildoptima.repository.user.UserRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith({SpringExtension.class})
@ActiveProfiles("test")
@Import({
  FaqCategoryEndpointUris.class,
  FaqCategoryResultActions.class
})
class FaqCategoryControllerTest {

  @Autowired private UserRepository userRepository;
  @Autowired private FaqCategoryRepository faqCategoryRepository;
  @Autowired private PasswordEncoder encoder;
  @Autowired private FaqCategoryResultActions resultActions;
  private FaqCategoryControllerTestParameters testParameters;

  @BeforeEach
  void setUp() {
    testParameters = new FaqCategoryControllerTestParameters(userRepository);
    List<User> users = testParameters.users();
    users.forEach(user -> user.setPassword(encoder.encode(user.getPassword())));
    userRepository.saveAll(users);

    faqCategoryRepository.saveAll(testParameters.faqCategories());
  }

  @AfterEach
  void tearDown() {
    faqCategoryRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void successfulRetrievalOfAllCategories() throws Exception {
    User moderatorUser = getUserByRole(MODERATOR);
    assumeFalse(moderatorUser == null);

    resultActions
        .getAll(moderatorUser)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.*", hasSize(faqCategoryRepository.findAll().size())));
  }

  @Test
  void failedRetrievalOfAllCategoriesAsAccessDenied() throws Exception {
    User clientUser = getUserByRole(CLIENT);
    assumeFalse(clientUser == null);

    resultActions.getAll(clientUser).andExpect(status().isForbidden());
  }

  @Test
  void successfulRetrievalOfCategoryById() throws Exception {
    User moderatorUser = getUserByRole(MODERATOR);
    assumeFalse(moderatorUser == null);
    FaqCategory faqCategory = faqCategoryRepository.findAll().stream().findAny().orElse(null);
    assumeFalse(faqCategory == null);

    resultActions
        .getById(faqCategory.getId(), moderatorUser)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(faqCategory.getId().toString()))
        .andExpect(jsonPath("$.name").value(faqCategory.getName()));
  }

  @Test
  void failedRetrievalOfCategoryByIdAsCategoryNotFound() throws Exception {
    User moderatorUser = getUserByRole(MODERATOR);
    assumeFalse(moderatorUser == null);

    resultActions.getById(UUID.randomUUID(), moderatorUser).andExpect(status().isNotFound());
  }

  @Test
  void successfulCreationOfFaqCategory() throws Exception {
    User moderatorUser = getUserByRole(MODERATOR);
    assumeFalse(moderatorUser == null);
    FaqCategoryRequestDto requestDto = testParameters.getFaqCategoryToSave();

    resultActions
        .create(moderatorUser, requestDto)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNotEmpty());
  }

  @Test
  void failedCreationOfFaqCategoryAsCategoryNameAlreadyExists() throws Exception {
    User moderatorUser = getUserByRole(MODERATOR);
    assumeFalse(moderatorUser == null);
    FaqCategoryRequestDto requestDto = testParameters.getFaqCategoryWithDuplicatedNameToSave();

    resultActions.create(moderatorUser, requestDto).andExpect(status().isConflict());
  }

  @Test
  void failedCreationOfFaqCategoryAsCategoryIsInvalid() throws Exception {
    User moderatorUser = getUserByRole(MODERATOR);
    assumeFalse(moderatorUser == null);
    FaqCategoryRequestDto requestDto = new FaqCategoryRequestDto();

    resultActions.create(moderatorUser, requestDto).andExpect(status().isBadRequest());
  }

  @Test
  void failedCreationOfFaqCategoryAsAccessDenied() throws Exception {
    User clientUser = getUserByRole(CLIENT);
    assumeFalse(clientUser == null);
    FaqCategoryRequestDto requestDto = testParameters.getFaqCategoryToSave();

    resultActions.create(clientUser, requestDto).andExpect(status().isForbidden());
  }

  @Test
  void successfulUpdateOfFaqCategory() throws Exception {
    User moderatorUser = getUserByRole(MODERATOR);
    assumeFalse(moderatorUser == null);
    FaqCategory faqCategory = faqCategoryRepository.findAll().stream().findAny().orElse(null);
    assumeFalse(faqCategory == null);
    FaqCategoryRequestDto requestDto =
        testParameters.getFaqCategoryToSave().toBuilder().name("Payment").build();

    resultActions
        .update(faqCategory.getId(), moderatorUser, requestDto)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(requestDto.getName()));
  }

  @Test
  void failedUpdateOfFaqCategoryAsCategoryNotFound() throws Exception {
    User moderatorUser = getUserByRole(MODERATOR);
    assumeFalse(moderatorUser == null);
    FaqCategoryRequestDto requestDto =
        testParameters.getFaqCategoryToSave().toBuilder().name("Payment").build();

    resultActions
        .update(UUID.randomUUID(), moderatorUser, requestDto)
        .andExpect(status().isNotFound());
  }

  @Test
  void successfulDeletionOfFaqCategory() throws Exception {
    User moderatorUser = getUserByRole(MODERATOR);
    assumeFalse(moderatorUser == null);
    FaqCategory faqCategory = faqCategoryRepository.findAll().stream().findAny().orElse(null);
    assumeFalse(faqCategory == null);

    resultActions.deleteById(faqCategory.getId(), moderatorUser).andExpect(status().isOk());
    assertFalse(faqCategoryRepository.existsById(faqCategory.getId()));
  }

  @Test
  void failedDeletionOfFaqCategory() throws Exception {
    User moderatorUser = getUserByRole(MODERATOR);
    assumeFalse(moderatorUser == null);

    resultActions.deleteById(UUID.randomUUID(), moderatorUser).andExpect(status().isNotFound());
  }

  @Test
  void successfulFetchingOfCategories() throws Exception {
    FetchRequestDto requestDto = testParameters.getFetchRequest();
    User moderatorUser = getUserByRole(MODERATOR);
    assumeFalse(moderatorUser == null);

    resultActions.fetch(requestDto, moderatorUser).andExpect(status().isOk());
  }

  @Test
  void failedFetchingOfCategoriesAsPermissionDenied() throws Exception {
    FetchRequestDto requestDto = testParameters.getFetchRequest();
    User clientUser = getUserByRole(CLIENT);

    resultActions.fetch(requestDto, clientUser).andExpect(status().isForbidden());
  }

  @Test
  void failedFetchingOfCategoriesAsRequestDtoIsInvalid() throws Exception {
    FetchRequestDto requestDto = testParameters.getInvalidFetchRequest();
    User moderatorUser = getUserByRole(MODERATOR);

    resultActions.fetch(requestDto, moderatorUser).andExpect(status().isBadRequest());
  }

  @Test
  void successfulExportingOfCategoriesInCsv() throws Exception {
    User moderatorUser = getUserByRole(MODERATOR);

    resultActions
        .getAllInCsv(moderatorUser)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", notNullValue()));
  }

  @Test
  void successfulGettingMetadata() throws Exception {
    User moderatorUser = getUserByRole(MODERATOR);
    FaqCategory lastUpdatedCategory =
        faqCategoryRepository.findTopByOrderByUpdatedAtDesc().orElse(null);
    assumeFalse(lastUpdatedCategory == null);

    resultActions
        .getMetadata(moderatorUser)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lastUpdatedAt").value(lastUpdatedCategory.getUpdatedAt().toString()))
        .andExpect(jsonPath("$.allActiveCount").value(faqCategoryRepository.count()));
  }

  private User getUserByRole(Role role) {
    return userRepository.findByRole(role).orElse(null);
  }
}
