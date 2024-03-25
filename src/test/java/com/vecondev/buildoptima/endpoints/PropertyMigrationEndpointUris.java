package com.vecondev.buildoptima.endpoints;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;

@Setter
@TestConfiguration
@ConfigurationProperties(prefix = "config.uris.property-migration")
public class PropertyMigrationEndpointUris implements EndpointUris {

  private String baseUri;
  private String migrationUri;
  private String reprocessUri;
  private String trackProgressUri;
  private String findByAinUri;

  @Override
  public String getDeleteByIdUri() {
    return null;
  }

  @Override
  public String getUpdateUri() {
    return null;
  }

  @Override
  public String getCreationUri() {
    return null;
  }

  @Override
  public String getFetchUri() {
    return null;
  }

  @Override
  public String getRetrieveByIdUri() {
    return null;
  }

  @Override
  public String getAllUri() {
    return null;
  }

  @Override
  public String getExportInCsvUri() {
    return null;
  }

  @Override
  public String getMetadataUri() {
    return null;
  }

  public String getMigrationUri() {
    return baseUri + migrationUri;
  }

  public String getReprocessUri() {
    return baseUri + reprocessUri;
  }

  public String getByAinUri() {
    return baseUri + findByAinUri;
  }

  public String getTrackProgressUri() {
    return baseUri + trackProgressUri;
  }
}
