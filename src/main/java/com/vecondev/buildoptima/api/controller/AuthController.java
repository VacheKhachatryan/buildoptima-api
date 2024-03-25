package com.vecondev.buildoptima.api.controller;

import com.vecondev.buildoptima.api.AuthApi;
import com.vecondev.buildoptima.dto.user.request.AuthRequestDto;
import com.vecondev.buildoptima.dto.user.request.ConfirmEmailRequestDto;
import com.vecondev.buildoptima.dto.user.request.RefreshTokenRequestDto;
import com.vecondev.buildoptima.dto.user.request.RestorePasswordRequestDto;
import com.vecondev.buildoptima.dto.user.request.UserRegistrationRequestDto;
import com.vecondev.buildoptima.dto.user.response.AuthResponseDto;
import com.vecondev.buildoptima.dto.user.response.RefreshTokenResponseDto;
import com.vecondev.buildoptima.dto.user.response.UserResponseDto;
import com.vecondev.buildoptima.service.auth.AuthService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController implements AuthApi {

  private final AuthService authService;

  @Override
  @PostMapping("/registration")
  public ResponseEntity<UserResponseDto> register(
      @Valid @RequestBody UserRegistrationRequestDto userRegistrationRequestDto) {
    log.info("Attempt to register user with email: {}", userRegistrationRequestDto.getEmail());

    return new ResponseEntity<>(
        authService.register(userRegistrationRequestDto), HttpStatus.CREATED);
  }

  @Override
  @PutMapping("/activation")
  public ResponseEntity<UserResponseDto> activate(@RequestParam("token") String token) {
    return ResponseEntity.ok(authService.activate(token));
  }

  @Override
  @PostMapping
  public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid AuthRequestDto authRequestDto) {
    final AuthResponseDto response = authService.login(authRequestDto);
    return ResponseEntity.ok(response);
  }

  @Override
  @PostMapping("/refreshment")
  public ResponseEntity<RefreshTokenResponseDto> refreshToken(
      @RequestBody @Valid RefreshTokenRequestDto refreshTokenRequestDto) {
    final RefreshTokenResponseDto response = authService.refreshToken(refreshTokenRequestDto);
    return ResponseEntity.ok(response);
  }

  @Override
  @PostMapping("/verification")
  public ResponseEntity<Void> verify(@RequestBody ConfirmEmailRequestDto email) {
    authService.verify(email);
    return ResponseEntity.ok().build();
  }

  @Override
  @PutMapping("/password/restore")
  public ResponseEntity<Void> restorePassword(
      @RequestBody @Valid RestorePasswordRequestDto restorePasswordRequestDto) {
    authService.restorePassword(restorePasswordRequestDto);
    return ResponseEntity.ok().build();
  }
}
