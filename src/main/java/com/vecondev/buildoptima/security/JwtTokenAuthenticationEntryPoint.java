package com.vecondev.buildoptima.security;

import static com.vecondev.buildoptima.util.JsonUtil.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.vecondev.buildoptima.exception.ApiError;
import com.vecondev.buildoptima.exception.Error;
import java.io.IOException;
import java.time.Instant;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    sendResponse(Error.valueOf(response.getHeader("error")), response);
  }

  private void sendResponse(Error error, HttpServletResponse response) throws IOException {
    ApiError apiError =
        new ApiError(error.getHttpStatus(), error.getCode(), Instant.now(), error.getMessage());
    response.setContentType(APPLICATION_JSON_VALUE);
    response.setStatus(error.getHttpStatus().value());
    response.getWriter().write(writeToJson(apiError));
  }
}
