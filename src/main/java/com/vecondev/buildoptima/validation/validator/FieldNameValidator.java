package com.vecondev.buildoptima.validation.validator;

import static com.vecondev.buildoptima.exception.Error.INVALID_SORTING_FIELD;

import com.vecondev.buildoptima.exception.InvalidFieldException;
import com.vecondev.buildoptima.filter.model.SortDto;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;

public class FieldNameValidator {

  private FieldNameValidator() {}

  public static void validateFieldNames(
      Map<String, ?> fieldDefinitionMap, @NotNull List<SortDto> sort) {
    sort.forEach(
        sortDto -> {
          if (!fieldDefinitionMap.containsKey(sortDto.getField())) {
            throw new InvalidFieldException(INVALID_SORTING_FIELD);
          }
        });
  }
}
