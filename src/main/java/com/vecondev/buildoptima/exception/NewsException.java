package com.vecondev.buildoptima.exception;

import lombok.Getter;

@Getter
public class NewsException extends BaseException {

  public NewsException(Error error) {
    super(error);
  }
}
