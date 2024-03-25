package com.vecondev.buildoptima.service.auth;

import com.vecondev.buildoptima.model.user.RefreshToken;
import java.util.UUID;

public interface RefreshTokenService {

  RefreshToken create(UUID userId);

  void deleteExpiredOnes();

  RefreshToken findByUserId(UUID id);

  RefreshToken findByRefreshToken(String refreshToken);
}
