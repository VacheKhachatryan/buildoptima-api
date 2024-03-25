package com.vecondev.buildoptima.exception;

public class DataIntegrityViolationException extends BaseException {

  public DataIntegrityViolationException(Error error) {
    super(error);
  }
}
