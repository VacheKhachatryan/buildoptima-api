package com.vecondev.buildoptima.service.auth.impl;

import com.vecondev.buildoptima.security.user.AppUserDetails;
import com.vecondev.buildoptima.service.auth.SecurityContextService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityContextServiceImpl implements SecurityContextService {

  @Override
  public AppUserDetails getUserDetails() {
    return (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  @Override
  public void setAuthentication(UsernamePasswordAuthenticationToken auth) {
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  @Override
  public void clearAuthentication() {
    SecurityContextHolder.clearContext();
  }
}
