package com.vecondev.buildoptima.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {

  @Schema(example = "2022-06-24 13:29:00.887950")
  private Instant lastUpdatedAt;

  private EntityOverview lastUpdatedBy;

  @Schema(example = "15")
  private Long allActiveCount;

  @Schema(example = "5")
  private Long allArchivedCount;
}
