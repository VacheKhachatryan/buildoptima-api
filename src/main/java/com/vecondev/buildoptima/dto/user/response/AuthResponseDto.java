package com.vecondev.buildoptima.dto.user.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {

  private UUID userId;

  private String accessToken;

  private String refreshToken;
}
