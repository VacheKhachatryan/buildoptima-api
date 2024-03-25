package com.vecondev.buildoptima.endpoints;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;

@Setter
@TestConfiguration
@ConfigurationProperties(prefix = "config.uris.news")
public class NewsEndpointUris implements EndpointUris {

  private String baseUri;
  private String deleteUri;
  private String updateUri;
  private String getByIdUri;
  private String getMetadataUri;
  private String exportCsvUri;
  private String fetchUri;

  private String archiveUri;

  @Override
  public String getDeleteByIdUri() {
    return baseUri + deleteUri;
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
    return baseUri + getByIdUri;
  }

  @Override
  public String getAllUri() {
    return null;
  }

  @Override
  public String getExportInCsvUri() {
    return baseUri + exportCsvUri;
  }

  @Override
  public String getMetadataUri() {
    return baseUri + getMetadataUri;
  }

  public String getArchiveUri() {
    return baseUri + archiveUri;
  }
}
