package com.vecondev.buildoptima.actions;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vecondev.buildoptima.endpoints.FaqCategoryEndpointUris;
import com.vecondev.buildoptima.endpoints.FaqQuestionEndpointUris;
import com.vecondev.buildoptima.filter.model.DictionaryField;
import com.vecondev.buildoptima.model.Status;
import com.vecondev.buildoptima.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@TestConfiguration
@RequiredArgsConstructor
public class FaqQuestionResultActions extends EntityResultActions<FaqQuestionEndpointUris> {

  @Override
  @Autowired
  protected void initialize(FaqQuestionEndpointUris endpointUris, MockMvc mockMvc,
      ObjectMapper objectMapper) {
    super.initialize(endpointUris, mockMvc, objectMapper);
  }

  public ResultActions lookup(Status status, DictionaryField dictionary, User user)
      throws Exception {
    return mockMvc
        .perform(
            addAuthorizationHeaders(get(endpointUris.getLookupUri(), status, dictionary), user)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON_VALUE));
  }
}
