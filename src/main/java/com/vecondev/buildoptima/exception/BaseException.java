package com.vecondev.buildoptima.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException {

  protected final Error error;

  public BaseException(Error error) {
    this.error = error;
  }
}
