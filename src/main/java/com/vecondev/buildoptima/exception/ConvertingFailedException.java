package com.vecondev.buildoptima.exception;

public class ConvertingFailedException extends BaseException {

  public ConvertingFailedException(Error error) {
    super(error);
  }
}
