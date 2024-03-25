package com.vecondev.buildoptima.repository.user;

import com.vecondev.buildoptima.model.user.ConfirmationToken;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, UUID> {

  Optional<ConfirmationToken> findByToken(String token);

  void deleteByUserId(UUID userId);

  void deleteByExpiresAtBefore(LocalDateTime now);
}
