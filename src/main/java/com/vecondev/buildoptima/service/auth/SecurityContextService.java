package com.vecondev.buildoptima.service.auth;

import com.vecondev.buildoptima.security.user.AppUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface SecurityContextService {

  AppUserDetails getUserDetails();

  void setAuthentication(final UsernamePasswordAuthenticationToken auth);

  void clearAuthentication();
}
