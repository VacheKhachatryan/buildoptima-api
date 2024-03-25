package com.vecondev.buildoptima.util;

import com.vecondev.buildoptima.exception.Error;
import com.vecondev.buildoptima.exception.ResourceNotFoundException;
import java.util.Objects;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RestPreconditions {

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * @throws ResourceNotFoundException if {@code object} is null
   */
  public <T> void checkNotNull(final T object, final Error errorCode) {
    if (Objects.isNull(object)) {
      throw new ResourceNotFoundException(errorCode);
    }
  }
}
