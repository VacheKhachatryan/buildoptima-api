package com.vecondev.buildoptima.dto.faq.response;

import com.vecondev.buildoptima.dto.EntityOverview;
import com.vecondev.buildoptima.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class FaqQuestionResponseDto {

  @Schema(example = "2635b586-d0d7-4a2d-b4b5-c98377a02322")
  private UUID id;

  @Schema(example = "How can I recover my password?")
  private String question;

  @Schema(
      example = """
          Click 'Forgot password' button inside the 'Sign up' form and
          write your email address in order to recover the account.""")
  private String answer;

  @Schema(example = "ACTIVE")
  private Status status;

  private EntityOverview category;

  private EntityOverview updatedBy;

  @Schema(example = "2022-06-24 13:29:00.887950")
  private Instant createdAt;

  @Schema(example = "2022-06-24 13:29:00.887950")
  private Instant updatedAt;
}
