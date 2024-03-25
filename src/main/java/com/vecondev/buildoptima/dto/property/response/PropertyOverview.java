package com.vecondev.buildoptima.dto.property.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PropertyOverview {

  @Schema(example = "5482003013")
  private String  ain;

  @Schema(example = "2516 Banyan Dr Los Angeles CA 90049")
  private String address;

}
