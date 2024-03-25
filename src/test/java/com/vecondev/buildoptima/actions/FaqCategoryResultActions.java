package com.vecondev.buildoptima.actions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vecondev.buildoptima.endpoints.FaqCategoryEndpointUris;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@TestConfiguration
@RequiredArgsConstructor
public class FaqCategoryResultActions extends EntityResultActions<FaqCategoryEndpointUris> {

  @Override
  @Autowired
  protected void initialize(FaqCategoryEndpointUris endpointUris, MockMvc mockMvc,
      ObjectMapper objectMapper) {
    super.initialize(endpointUris, mockMvc, objectMapper);
  }
}
