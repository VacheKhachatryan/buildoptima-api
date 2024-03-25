package com.vecondev.buildoptima.service.auth.impl;

import static com.vecondev.buildoptima.exception.Error.CONFIRM_TOKEN_NOT_FOUND;

import com.vecondev.buildoptima.exception.AuthenticationException;
import com.vecondev.buildoptima.model.user.ConfirmationToken;
import com.vecondev.buildoptima.model.user.User;
import com.vecondev.buildoptima.repository.user.ConfirmationTokenRepository;
import com.vecondev.buildoptima.repository.user.UserRepository;
import com.vecondev.buildoptima.service.auth.ConfirmationTokenService;
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
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

  private final ConfirmationTokenRepository confirmationTokenRepository;
  private final UserRepository userRepository;

  @Override
  public ConfirmationToken create(User user) {
    ConfirmationToken confirmationToken =
        ConfirmationToken.builder()
            .token(UUID.randomUUID().toString())
            .expiresAt(LocalDateTime.now().plusDays(1))
            .user(user)
            .build();
    return confirmationTokenRepository.save(confirmationToken);
  }

  @Override
  public ConfirmationToken getByToken(String token) {
    return confirmationTokenRepository
        .findByToken(token)
        .orElseThrow(() -> new AuthenticationException(CONFIRM_TOKEN_NOT_FOUND));
  }

  @Override
  public void remove(UUID userId) {
    confirmationTokenRepository.deleteByUserId(userId);
    log.info("Confirmation token was deleted, because user has verified the email");
  }

  @Override
  @Scheduled(cron = "0 */12 * * * *")
  public void deleteExpiredOnes() {
    confirmationTokenRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    log.info("Expired confirmation tokens have been removed");
  }

  @Override
  public void deleteByUserId(UUID userId) {
    confirmationTokenRepository.deleteByUserId(userId);
  }
}
