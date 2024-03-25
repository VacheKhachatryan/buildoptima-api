package com.vecondev.buildoptima.filter.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Criteria {
  private SearchOperation operation;

  private String name;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String value;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<String> values;

  public Criteria(SearchOperation operation, String name, String value) {
    this.operation = operation;
    this.name = name;
    this.value = value;
  }

  public Criteria(SearchOperation operation, String name, List<String> values) {
    this.operation = operation;
    this.name = name;
    this.values = values;
  }
}
