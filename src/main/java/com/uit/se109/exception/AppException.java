package com.uit.se109.exception;

import java.util.Map;
import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

  private ErrorCode errorCode;
  private Map<String, String> details;

  public AppException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
