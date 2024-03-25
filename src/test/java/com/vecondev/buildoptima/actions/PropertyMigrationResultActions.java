package com.vecondev.buildoptima.actions;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vecondev.buildoptima.endpoints.NewsEndpointUris;
import com.vecondev.buildoptima.endpoints.PropertyMigrationEndpointUris;
import com.vecondev.buildoptima.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@TestConfiguration
@RequiredArgsConstructor
public class PropertyMigrationResultActions
    extends EntityResultActions<PropertyMigrationEndpointUris> {

  @Override
  @Autowired
  protected void initialize(PropertyMigrationEndpointUris endpointUris, MockMvc mockMvc,
      ObjectMapper objectMapper) {
    super.initialize(endpointUris, mockMvc, objectMapper);
  }

  public ResultActions migrate(User user) throws Exception {
    return getPostRequest(user, endpointUris.getMigrationUri());
  }

  public ResultActions reprocess(User user) throws Exception {
    return getPostRequest(user, endpointUris.getReprocessUri());
  }

  public ResultActions trackProgress(User user) throws Exception {
    return mockMvc.perform(
        addAuthorizationHeaders(get(endpointUris.getTrackProgressUri()), user)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON_VALUE));
  }

  private ResultActions getPostRequest(User user, String uri) throws Exception {
    return mockMvc.perform(
        addAuthorizationHeaders(post(uri), user)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON_VALUE));
  }
}
