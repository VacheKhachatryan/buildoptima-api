package com.vecondev.buildoptima.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends BaseException {

  public ResourceNotFoundException(Error error) {
    super(error);
  }
}
