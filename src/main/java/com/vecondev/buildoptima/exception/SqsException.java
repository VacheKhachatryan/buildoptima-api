package com.vecondev.buildoptima.exception;

public class SqsException extends BaseException {

  public SqsException(Error error) {
    super(error);
  }
}