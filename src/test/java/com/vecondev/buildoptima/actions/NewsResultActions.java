package com.vecondev.buildoptima.actions;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.news.request.NewsCreateRequestDto;
import com.vecondev.buildoptima.dto.news.request.NewsUpdateRequestDto;
import com.vecondev.buildoptima.endpoints.FaqCategoryEndpointUris;
import com.vecondev.buildoptima.endpoints.NewsEndpointUris;
import com.vecondev.buildoptima.model.user.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@TestConfiguration
@RequiredArgsConstructor
public class NewsResultActions extends EntityResultActions<NewsEndpointUris> {

  @Override
  @Autowired
  protected void initialize(NewsEndpointUris endpointUris, MockMvc mockMvc,
      ObjectMapper objectMapper) {
    super.initialize(endpointUris, mockMvc, objectMapper);
  }

  @Override
  public ResultActions create(User user, Object requestDto) throws Exception {
    NewsCreateRequestDto createRequestDto = (NewsCreateRequestDto) requestDto;
    return mockMvc.perform(
        addAuthorizationHeaders(multipart(endpointUris.getCreationUri()), user)
            .content(createRequestDto.getImage().getBytes())
            .param("title", createRequestDto.getTitle())
            .param("summary", createRequestDto.getSummary())
            .param("description", createRequestDto.getDescription())
            .param("category", createRequestDto.getCategory())
            .param("keywords", " "));
  }

  @Override
  public ResultActions update(UUID entityId, User user, Object requestDto) throws Exception {
    NewsUpdateRequestDto updateRequestDto = (NewsUpdateRequestDto) requestDto;
    return mockMvc.perform(
        addAuthorizationHeaders(patch(endpointUris.getUpdateUri(), entityId), user)
            .content(updateRequestDto.getImage().getBytes())
            .contentType(MULTIPART_FORM_DATA_VALUE)
            .param("title", updateRequestDto.getTitle())
            .param("summary", updateRequestDto.getSummary())
            .param("description", updateRequestDto.getDescription())
            .param("category", updateRequestDto.getCategory())
            .param("keywords", " "));
  }

  public ResultActions getMetadata(User user) throws Exception {
    return mockMvc.perform(addAuthorizationHeaders(get(endpointUris.getMetadataUri()), user));
  }

  public ResultActions getAllInCsv(FetchRequestDto fetchRequest, User user) throws Exception {
    return mockMvc.perform(
        addAuthorizationHeaders(post(endpointUris.getExportInCsvUri()), user)
            .content(asJsonString(fetchRequest))
            .contentType(APPLICATION_JSON)
            .accept("application/csv"));
  }

  public ResultActions archive(UUID id, User user) throws Exception {
    return mockMvc.perform(
        addAuthorizationHeaders(patch(endpointUris.getArchiveUri(), id), user));
  }
}
