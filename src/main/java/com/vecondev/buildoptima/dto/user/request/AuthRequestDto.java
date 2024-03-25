package com.vecondev.buildoptima.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDto {

  @NotBlank
  @Schema(example = "example@gmail.com")
  private String username;

  @NotBlank
  @Schema(example = "Example1234.")
  private String password;
}
