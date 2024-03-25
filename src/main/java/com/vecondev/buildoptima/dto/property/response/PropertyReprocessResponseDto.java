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
public class PropertyReprocessResponseDto {

  @Schema(
      description = "All files' count that have been re-processed during this request.",
      example = "3")
  private int allSuccessfullyReprocessedFiles;

  @Schema(
      description =
          "All file names that have been re-failed while re-processing and it's failure reasons.",
      example = "{ \"116.json.gz\": \"Not in GZIP format.\"}")
  private Map<String, String> allFailedFilesToProcess;
}
