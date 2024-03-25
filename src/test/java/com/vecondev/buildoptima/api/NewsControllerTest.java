package com.vecondev.buildoptima.api;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vecondev.buildoptima.actions.NewsResultActions;
import com.vecondev.buildoptima.config.S3TestConfig;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.news.request.NewsCreateRequestDto;
import com.vecondev.buildoptima.dto.news.request.NewsUpdateRequestDto;
import com.vecondev.buildoptima.endpoints.NewsEndpointUris;
import com.vecondev.buildoptima.model.news.News;
import com.vecondev.buildoptima.model.user.User;
import com.vecondev.buildoptima.parameters.news.NewsControllerTestParameters;
import com.vecondev.buildoptima.parameters.news.NewsServiceTestParameters;
import com.vecondev.buildoptima.repository.news.NewsRepository;
import com.vecondev.buildoptima.repository.user.UserRepository;
import java.util.Set;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith({SpringExtension.class})
@ActiveProfiles("test")
@Import({NewsEndpointUris.class, NewsResultActions.class})
class NewsControllerTest {
  @Autowired private UserRepository userRepository;
  @Autowired private NewsRepository newsRepository;
  @Autowired private NewsResultActions newsResultActions;
  private NewsControllerTestParameters newsControllerTestParameters;

  private NewsServiceTestParameters newsServiceTestParameters;

  private User admin;
  private User client;
  private News news;

  @BeforeEach
  void init() {
    newsControllerTestParameters = new NewsControllerTestParameters();
    newsServiceTestParameters = new NewsServiceTestParameters();
    admin = newsControllerTestParameters.getRightUser();
    client = newsControllerTestParameters.getWrongUser();
    userRepository.saveAllAndFlush(Set.of(admin, client));
    NewsCreateRequestDto createNewsRequestDto = newsControllerTestParameters.getRequestToSave();
    News newsToSave = newsControllerTestParameters.getSavedNewsItem(createNewsRequestDto);
    newsToSave.setCreatedBy(admin.getId());
    newsToSave.setUpdatedBy(admin.getId());
    news = newsRepository.saveAndFlush(newsToSave);
  }

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
    newsRepository.deleteAll();
  }

  @Test
  void createNewsSuccess() throws Exception {
    NewsCreateRequestDto createRequestDto = newsControllerTestParameters.createRequestDto();
    newsResultActions.create(admin, createRequestDto).andExpect(status().isCreated());
  }

  @Test
  void failedCreateNewsAsContainsInvalidFields() throws Exception {
    NewsCreateRequestDto createRequestDto =
        newsControllerTestParameters.createRequestDtoWithInvalidFields();
    newsResultActions.create(admin, createRequestDto).andExpect(status().isBadRequest());
  }

  @Test
  void failedCreateNewsAsPermissionDenied() throws Exception {
    NewsCreateRequestDto createRequestDto = newsControllerTestParameters.createRequestDto();
    newsResultActions.create(client, createRequestDto).andExpect(status().isForbidden());
  }

  @Test
  void updateNewsSuccess() throws Exception {
    NewsUpdateRequestDto updateRequestDto = newsControllerTestParameters.updateRequestDto();
    newsResultActions.update(news.getId(), admin, updateRequestDto).andExpect(status().isOk());
  }

  @Test
  void failedUpdateAsInvalidFields() throws Exception {
    NewsUpdateRequestDto updateRequestDto =
        newsControllerTestParameters.updateRequestDtoWithInvalidFields();
    newsResultActions
        .update(news.getId(), admin, updateRequestDto)
        .andExpect(status().isBadRequest());
  }

  @Test
  void failedUpdateAsNewsItemNotFound() throws Exception {
    NewsUpdateRequestDto updateRequestDto = newsControllerTestParameters.updateRequestDto();
    newsResultActions
        .update(UUID.randomUUID(), admin, updateRequestDto)
        .andExpect(status().isNotFound());
  }

  @Test
  void deleteAsNewsItemSuccess() throws Exception {
    newsResultActions.deleteById(news.getId(), admin).andExpect(status().isOk());
  }

  @Test
  void failedDeleteAsNewsItemNotFound() throws Exception {
    newsResultActions.deleteById(UUID.randomUUID(), admin).andExpect(status().isNotFound());
  }

  @Test
  void failedDeleteAsAccessDenied() throws Exception {
    newsResultActions.deleteById(news.getId(), client).andExpect(status().isForbidden());
  }

  @Test
  void getByIdSuccess() throws Exception {
    newsResultActions.getById(news.getId(), admin).andExpect(status().isOk());
  }

  @Test
  void failedGetByIdAsNotFound() throws Exception {
    newsResultActions.getById(UUID.randomUUID(), admin).andExpect(status().isNotFound());
  }

  @Test
  void getMetadataSuccess() throws Exception {
    newsResultActions.getMetadata(admin).andExpect(status().isOk());
  }

  @Test
  void exportCsvSuccess() throws Exception {
    FetchRequestDto fetchRequest = newsServiceTestParameters.getFetchRequest();
    newsResultActions.getAllInCsv(fetchRequest, admin).andExpect(status().isOk());
  }

  @Test
  void fetchNewsSuccess() throws Exception {
    FetchRequestDto fetchRequest = newsServiceTestParameters.getFetchRequest();
    newsResultActions.fetch(fetchRequest, admin).andExpect(status().isOk());
  }

  @Test
  void archiveNewsSuccess() throws Exception {
    newsResultActions.archive(news.getId(), admin).andExpect(status().isOk());
  }

  @Test
  void failedArchiveNewsAsNotFound() throws Exception {
    newsResultActions.archive(UUID.randomUUID(), admin).andExpect(status().isNotFound());
  }

  @Test
  void failedArchiveNewsAsAccessDenied() throws Exception {
    newsResultActions.archive(news.getId(), client).andExpect(status().isForbidden());
  }
}
