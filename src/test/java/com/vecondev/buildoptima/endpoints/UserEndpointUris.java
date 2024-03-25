package com.vecondev.buildoptima.endpoints;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;

@Setter
@TestConfiguration
@ConfigurationProperties(prefix = "config.uris.user")
public class UserEndpointUris implements EndpointUris {

  private String baseUri;
  private String fetchUri;
  private String retrieveByIdUri;
  private String deleteImageByIdUri;
  private String editUserUri;
  private String downloadImageUri;
  private String uploadImageUri;
  private String restorePasswordUri;
  private String verifyPasswordUri;
  private String changePasswordUri;
  private String refreshTokenUri;
  private String activationUri;
  private String loginUri;
  private String registrationUri;

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
    return baseUri + fetchUri;
  }

  @Override
  public String getRetrieveByIdUri() {
    return baseUri + retrieveByIdUri;
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

  public String getImageDeletionUri() {
    return baseUri + deleteImageByIdUri;
  }

  public String getImageDownloadingUri() {
    return baseUri + downloadImageUri;
  }

  public String getImageUploadingUri() {
    return baseUri + uploadImageUri;
  }

  public String getPasswordRestoringUri() {
    return restorePasswordUri;
  }

  public String getPasswordVerificationUri() {
    return verifyPasswordUri;
  }

  public String getChangePasswordUri() {
    return baseUri + changePasswordUri;
  }

  public String getRefreshTokenUri() {
    return refreshTokenUri;
  }

  public String getLoginUri() {
    return loginUri;
  }

  public String getActivationUri() {
    return activationUri;
  }

  public String getRegistrationUri() {
    return registrationUri;
  }

  public String getEditUserUri() {
    return baseUri + editUserUri;
  }
}
