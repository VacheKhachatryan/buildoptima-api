package com.vecondev.buildoptima.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiError {

  @Schema(description = "Http response status", example = "CONFLICT")
  private HttpStatus status;

  @Schema(description = "The exception description", example = "4121")
  private Integer errorCode;

  @Schema(description = "Timestamp showing when the error occurred")
  private Instant timestamp;

  @Schema(
      description = "Error message",
      example = "There is an user registered with such an email!")
  private String message;
}
