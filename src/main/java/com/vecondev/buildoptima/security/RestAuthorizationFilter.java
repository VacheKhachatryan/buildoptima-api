package com.vecondev.buildoptima.security;

import static com.vecondev.buildoptima.util.JsonUtil.*;

import com.vecondev.buildoptima.exception.AuthenticationException;
import com.vecondev.buildoptima.manager.JwtTokenManager;
import com.vecondev.buildoptima.security.user.AppUserDetails;
import com.vecondev.buildoptima.service.auth.SecurityContextService;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestAuthorizationFilter extends OncePerRequestFilter {

  public static final String USER_ID = "user_id";
  public static final String USERNAME = "username";
  public static final String AUTHORITIES = "authorities";
  public static final String ERROR = "error";

  private final JwtTokenManager jwtTokenManager;
  private final SecurityContextService securityContextService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    try {
      String id = request.getHeader(USER_ID);
      String username = request.getHeader(USERNAME);
      String authoritiesInString = request.getHeader(AUTHORITIES);
      if (id != null
          && StringUtils.isNotBlank(username)
          && StringUtils.isNotBlank(authoritiesInString)) {
        List<SimpleGrantedAuthority> authorities =
            jwtTokenManager.authoritiesFromString(
                getListOfAuthoritiesFromJsonString(authoritiesInString));
        AppUserDetails userDetails =
            AppUserDetails.builder()
                .id(UUID.fromString(id))
                .username(request.getHeader(USERNAME))
                .authorities(authorities)
                .build();
        securityContextService.setAuthentication(
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()));
      }
      filterChain.doFilter(request, response);
    } catch (AuthenticationException ex) {
      response.addHeader(ERROR, ex.getError().name());
      response.setStatus(ex.getError().getHttpStatus().value());
    } finally {
      securityContextService.clearAuthentication();
    }
  }
}
