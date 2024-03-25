package com.vecondev.buildoptima.endpoints;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;

@Setter
@TestConfiguration
@ConfigurationProperties(prefix = "config.uris.faq-question")
public class FaqQuestionEndpointUris implements EndpointUris {

  private String baseUri;
  private String deleteByIdUri;
  private String updateUri;
  private String fetchUri;
  private String retrieveByIdUri;
  private String exportInCsvUri;
  private String metadataUri;
  private String lookupUri;

  @Override
  public String getDeleteByIdUri() {
    return baseUri + deleteByIdUri;
  }

  @Override
  public String getUpdateUri() {
    return baseUri + updateUri;
  }

  @Override
  public String getCreationUri() {
    return baseUri;
  }

  @Override
  public String getFetchUri() {
    return baseUri + fetchUri;
  }

  @Override
  public String getRetrieveByIdUri() {
    return baseUri + retrieveByIdUri;
  }

  @Override
  public String getAllUri() {
    return baseUri;
  }

  @Override
  public String getExportInCsvUri() {
    return baseUri + exportInCsvUri;
  }

  @Override
  public String getMetadataUri() {
    return baseUri + metadataUri;
  }

  public String getLookupUri() {
    return baseUri + lookupUri;
  }
}
