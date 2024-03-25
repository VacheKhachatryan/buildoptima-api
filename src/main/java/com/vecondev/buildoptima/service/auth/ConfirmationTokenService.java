package com.vecondev.buildoptima.service.auth;

import com.vecondev.buildoptima.model.user.ConfirmationToken;
import com.vecondev.buildoptima.model.user.User;
import java.util.UUID;

public interface ConfirmationTokenService {

  ConfirmationToken create(User user);

  ConfirmationToken getByToken(String token);

  void remove(UUID id);

  void deleteExpiredOnes();

  void deleteByUserId(UUID id);
}
