package com.vecondev.buildoptima.filter.converter;

import static com.vecondev.buildoptima.exception.Error.INVALID_STATUS;

import com.vecondev.buildoptima.exception.InvalidFieldException;
import com.vecondev.buildoptima.model.Status;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.lang.Nullable;

public class StatusConverter implements Converter<String, Status> {

  @Nullable
  @Override
  public Status convert(@NonNull String status) {
    try {
      return Status.valueOf(status);
    } catch (InvalidDataAccessApiUsageException ex) {
      throw new InvalidFieldException(INVALID_STATUS);
    }
  }
}
