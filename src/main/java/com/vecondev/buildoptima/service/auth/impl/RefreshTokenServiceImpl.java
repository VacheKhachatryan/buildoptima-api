package com.vecondev.buildoptima.service.auth.impl;

import com.vecondev.buildoptima.config.properties.JwtConfigProperties;
import com.vecondev.buildoptima.exception.AuthenticationException;
import com.vecondev.buildoptima.exception.Error;
import com.vecondev.buildoptima.model.user.RefreshToken;
import com.vecondev.buildoptima.repository.user.RefreshTokenRepository;
import com.vecondev.buildoptima.service.auth.RefreshTokenService;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

  private final JwtConfigProperties jwtConfigProperties;
  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  public RefreshToken create(UUID userId) {
    final String refreshTokenPlain = UUID.randomUUID().toString();
    final RefreshToken refreshToken =
        RefreshToken.builder()
            .userId(userId)
            .plainRefreshToken(refreshTokenPlain)
            .expiresAt(
                LocalDateTime.now().plusDays(jwtConfigProperties.getRefreshToken().getValidity()))
            .build();
    return refreshTokenRepository.saveAndFlush(refreshToken);
  }

  @Override
  public RefreshToken findByUserId(UUID id) {
    return refreshTokenRepository.findByUserId(id).orElse(null);
  }

  @Override
  public RefreshToken findByRefreshToken(String refreshToken) {
    return refreshTokenRepository
        .findById(refreshToken)
        .orElseThrow(() -> new AuthenticationException(Error.REFRESH_TOKEN_INVALID));
  }

  @Override
  @Scheduled(cron = "0 */12 * * * *")
  public void deleteExpiredOnes() {
    refreshTokenRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    log.info("Expired refresh tokens have been removed");
  }
}
