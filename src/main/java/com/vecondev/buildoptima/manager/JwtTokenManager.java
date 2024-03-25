package com.vecondev.buildoptima.manager;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.vecondev.buildoptima.config.properties.JwtConfigProperties;
import com.vecondev.buildoptima.model.user.User;
import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenManager {

  private static final String USER_ID = "user_id";
  private static final String AUTHORITIES = "authorities";
  private final CertificateManager certificateManager;
  private final JwtConfigProperties jwtConfigProperties;
  private Algorithm algorithm;
  private Integer accessTokenValidity;

  @PostConstruct
  private void init() {
    accessTokenValidity = jwtConfigProperties.getAccessToken().getValidity();
    algorithm = Algorithm.RSA256((RSAPrivateKey) certificateManager.privateKey());
  }

  public String generateAccessToken(User user) {
    log.info("Just created a new access token with {} minutes validity", accessTokenValidity);
    return JWT.create()
        .withSubject(user.getEmail())
        .withClaim(USER_ID, user.getId().toString())
        .withClaim(AUTHORITIES, authoritiesAsString(user.getRole().getAuthorities()))
        .withIssuer(jwtConfigProperties.getIssuer())
        .withIssuedAt(Instant.now())
        .withExpiresAt(Instant.now().plus(accessTokenValidity, ChronoUnit.MINUTES))
        .sign(algorithm);
  }

  public List<SimpleGrantedAuthority> authoritiesFromString(List<String> authorities) {
    return authorities.stream().map(SimpleGrantedAuthority::new).toList();
  }

  public List<String> authoritiesAsString(List<SimpleGrantedAuthority> authorities) {
    return authorities.stream().map(SimpleGrantedAuthority::getAuthority).toList();
  }
}
