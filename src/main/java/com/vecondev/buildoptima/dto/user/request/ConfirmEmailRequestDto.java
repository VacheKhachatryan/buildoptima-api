package com.vecondev.buildoptima.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmEmailRequestDto {

  @NotNull
  @Email
  @Schema(
      title = "User's email",
      description = "Should have valid email format",
      example = "example@gmail.com",
      required = true)
  private String email;
}
