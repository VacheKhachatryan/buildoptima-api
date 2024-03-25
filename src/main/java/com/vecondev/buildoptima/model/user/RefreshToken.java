package com.vecondev.buildoptima.model.user;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@Entity
@Table(name = "refresh_token")
public class RefreshToken {

  @Id
  @Column(name = "refresh_token")
  private String plainRefreshToken;

  @Column(name = "user_id")
  private UUID userId;

  @Column(name = "expires_at")
  private LocalDateTime expiresAt;

  public RefreshToken(String refreshToken, UUID userId, LocalDateTime expiresAt) {
    this.plainRefreshToken = refreshToken;
    this.userId = userId;
    this.expiresAt = expiresAt;
  }
}
