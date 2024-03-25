package com.vecondev.buildoptima.dto.property.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PropertyMigrationProgressResponseDto {

  @Schema(description = "All files' count that have been processed.", example = "4")
  private int allProcessedFiles;

  @Schema(
      description =
          "All file names that have been failed while processing and it's failure reasons.",
      example = "{ \"116.json.gz\": \"Not in GZIP format.\"}")
  private Map<String, String> allFailedFilesToProcess;

  @Schema(description = "All properties' count that have been migrated")
  private int allProcessedProperties;
}
