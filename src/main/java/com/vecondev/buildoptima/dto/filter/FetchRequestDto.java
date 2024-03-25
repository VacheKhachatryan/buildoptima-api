package com.vecondev.buildoptima.dto.filter;

import com.vecondev.buildoptima.filter.model.SortDto;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FetchRequestDto {

  Map<String, Object> filter;
  private Integer skip;
  private Integer take;
  private List<@Valid SortDto> sort;
}
