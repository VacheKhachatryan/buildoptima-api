package com.vecondev.buildoptima.dto.property;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyListDto {

  private List<PropertyReadDto> properties;
}
