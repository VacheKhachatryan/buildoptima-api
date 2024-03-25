package com.vecondev.buildoptima.dto.faq.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class FaqCategoryRequestDto {

  @NotBlank
  @Length(min = 2, max = 50)
  @Schema(
      title = "Faq category name",
      description = "Name's length should be between 2 and 50.",
      example = "Royalties & Statements",
      minLength = 2,
      maxLength = 50)
  private String name;
}
