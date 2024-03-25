package com.vecondev.buildoptima.dto.faq.request;

import com.vecondev.buildoptima.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class FaqQuestionRequestDto {

  @NotBlank
  @Length(min = 2, max = 300)
  @Schema(
      title = "Question",
      description = "Question's length should be between 2 and 300.",
      example = "How can I recover my password?",
      minLength = 2,
      maxLength = 300)
  private String question;

  @NotBlank
  @Length(min = 2, max = 300)
  @Schema(
      title = "Answer",
      description = "Answer's length should be between 2 and 300.",
      example = """
                Click 'Forgot password' button inside the 'Sign up' form
                and write your email address in order to recover the account.""",
      minLength = 2,
      maxLength = 300)
  private String answer;

  @NotNull
  @Schema(
      title = "Resource status",
      description = "Shows if the question is active or archived.",
      example = "ACTIVE")
  private Status status;

  @NotNull
  @Schema(title = "Category Id", example = "2635b586-d0d7-4a2d-b4b5-c98377a02322")
  private UUID faqCategoryId;
}
