package com.vecondev.buildoptima.repository.user;

import com.vecondev.buildoptima.model.user.RefreshToken;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

  Optional<RefreshToken> findByUserId(UUID userId);

  void deleteByExpiresAtBefore(LocalDateTime localDateTime);
}
