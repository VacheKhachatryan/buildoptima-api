package com.vecondev.buildoptima.filter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@AllArgsConstructor
@Schema(name = "Sort")
public class SortDto {

  @NotBlank String field;

  @NotNull Direction order;

  public enum Direction {
    @JsonProperty("asc")
    ASC,
    @JsonProperty("desc")
    DESC
  }
}
