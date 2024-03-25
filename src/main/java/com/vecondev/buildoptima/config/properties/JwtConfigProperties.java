package com.vecondev.buildoptima.config.properties;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "platform.security.jwt")
public class JwtConfigProperties {

  private String issuer;
  private String authorizationHeader;
  private String authorizationHeaderPrefix;

  private SignatureAlgorithm signatureAlgorithm;
  private RefreshToken refreshToken;
  private AccessToken accessToken;

  @Data
  public static class AccessToken {

    private Integer validity;
  }

  @Data
  public static class RefreshToken {

    private Long validity;
  }
}
