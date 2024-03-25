package com.vecondev.buildoptima.dto.news.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NewsCreateRequestDto {

  @Schema(
      title = "News title",
      description = "Title length must be between 5 and 20 characters",
      example = "Summer Sales",
      required = true,
      minLength = 5)
  @NotBlank(message = "News title may not be empty")
  private String title;

  @Schema(
      title = "News Short Summary",
      description = "Summary length must be between 25 and 50 characters",
      example = "Steam Summer Sale 2022 continues — save big on top rated PC games",
      required = true,
      minLength = 20)
  @NotBlank(message = "News summary may not be empty")
  private String summary;

  @Schema(title = "News Keywords")
  private List<String> keywords;

  @Schema(
      title = "News Description",
      description = "Description length must be between 50 and 250 characters",
      required = true,
      minLength = 50)
  @NotBlank(message = "News description may not be empty")
  private String description;

  @Schema(
      title = "News Category",
      description = "The category should be chosen by user while adding news item",
      required = true)
  @NotBlank(message = "News category may not be empty")
  private String category;

  @Schema(title = "News image")
  private MultipartFile image;
}
