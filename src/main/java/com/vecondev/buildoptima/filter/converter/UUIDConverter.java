package com.vecondev.buildoptima.filter.converter;

import java.util.UUID;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

public class UUIDConverter implements Converter<String, UUID> {
  @Override
  public UUID convert(@NonNull String userId) {
    return UUID.fromString(userId);
  }
}
