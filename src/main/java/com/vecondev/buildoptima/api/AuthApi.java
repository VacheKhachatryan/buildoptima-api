package com.vecondev.buildoptima.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.vecondev.buildoptima.dto.user.request.AuthRequestDto;
import com.vecondev.buildoptima.dto.user.request.ConfirmEmailRequestDto;
import com.vecondev.buildoptima.dto.user.request.RefreshTokenRequestDto;
import com.vecondev.buildoptima.dto.user.request.RestorePasswordRequestDto;
import com.vecondev.buildoptima.dto.user.request.UserRegistrationRequestDto;
import com.vecondev.buildoptima.dto.user.response.AuthResponseDto;
import com.vecondev.buildoptima.dto.user.response.RefreshTokenResponseDto;
import com.vecondev.buildoptima.dto.user.response.UserResponseDto;
import com.vecondev.buildoptima.exception.ApiError;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(
    name = "Authentication",
    description = "Endpoints for user authentication",
    externalDocs =
        @ExternalDocumentation(
            description = "Click here to see a detailed explanation of application errors",
            url =
                "https://github.com/vecondev/buildoptima-api/blob/develop/docs/application-errors.md"))
public interface AuthApi {

  @Operation(
      summary = "Register new user",
      description = "Possible error codes: 40011, 4092, 4093, 5001")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "New user should be registered.",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserResponseDto.class))),
        @ApiResponse(
            responseCode = "400",
            ref = "#/components/responses/MethodArgumentNotValidResponse"),
        @ApiResponse(
            responseCode = "409",
            description = "There already is an duplicate value either for email or phone number.",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)))
      })
  ResponseEntity<UserResponseDto> register(UserRegistrationRequestDto userRegistrationRequestDto);

  @Operation(summary = "Activate email", description = "Possible error codes: 4041")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "User account activated by email confirmation",
            content =
                @Content(
                    schema = @Schema(implementation = UserResponseDto.class),
                    mediaType = APPLICATION_JSON_VALUE)),
        @ApiResponse(
            responseCode = "404",
            description = "Email confirmation token is not found",
            content =
                @Content(
                    schema = @Schema(implementation = ApiError.class),
                    mediaType = APPLICATION_JSON_VALUE))
      })
  ResponseEntity<UserResponseDto> activate(String token);

  @Operation(summary = "User sign in", description = "Possible error codes: 40011, 4011")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "User signed in",
            content =
                @Content(
                    schema = @Schema(implementation = AuthResponseDto.class),
                    mediaType = APPLICATION_JSON_VALUE)),
        @ApiResponse(
            responseCode = "400",
            ref = "#/components/responses/MethodArgumentNotValidResponse"),
        @ApiResponse(
            responseCode = "401",
            description = "Bad credentials",
            content =
                @Content(
                    schema = @Schema(implementation = ApiError.class),
                    mediaType = APPLICATION_JSON_VALUE))
      })
  ResponseEntity<AuthResponseDto> login(AuthRequestDto authRequestDto);

  @Operation(summary = "Refresh tokens", description = "Possible error codes: 40012, 40013")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "New access token was created for user",
            content =
                @Content(
                    schema = @Schema(implementation = RefreshTokenResponseDto.class),
                    mediaType = APPLICATION_JSON_VALUE)),
        @ApiResponse(
            responseCode = "400",
            description = "The refresh token is either not provided or expired",
            content =
                @Content(
                    schema = @Schema(implementation = ApiError.class),
                    mediaType = APPLICATION_JSON_VALUE)),
      })
  ResponseEntity<RefreshTokenResponseDto> refreshToken(
      RefreshTokenRequestDto refreshTokenRequestDto);

  @Operation(
      summary = "Request to receive an email to restore the password",
      description = "Possible error codes: 4042, 5001")
  @RequestBody(
      content =
          @Content(
              schema = @Schema(implementation = ConfirmEmailRequestDto.class),
              mediaType = APPLICATION_JSON_VALUE))
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Email was sent to user"),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content =
                @Content(
                    schema = @Schema(implementation = ApiError.class),
                    mediaType = APPLICATION_JSON_VALUE))
      })
  ResponseEntity<Void> verify(ConfirmEmailRequestDto email);

  @Operation(
      summary = "Restore forgotten password",
      description = "Possible error codes: 40011, 4041")
  @RequestBody(
      content =
          @Content(
              schema = @Schema(implementation = RestorePasswordRequestDto.class),
              mediaType = APPLICATION_JSON_VALUE))
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "User password successfully changed"),
        @ApiResponse(
            responseCode = "400",
            ref = "#/components/responses/MethodArgumentNotValidResponse"),
        @ApiResponse(
            responseCode = "404",
            description = "Confirmation token not found",
            content =
                @Content(
                    schema = @Schema(implementation = ApiError.class),
                    mediaType = APPLICATION_JSON_VALUE))
      })
  ResponseEntity<Void> restorePassword(RestorePasswordRequestDto restorePasswordRequestDto);
}
