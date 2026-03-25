package com.uit.se109.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  UNCATEGORIZED_EXCEPTION(2000, "An uncategorized exception occurred.", 500),
  RESOURCE_NOT_FOUND(2001, "The requested resource was not found.", 404),
  VALIDATION_ERROR(2002, "Validation failed for the request.", 400),
  ;
  private final Integer code;
  private final String message;
  private final int httpStatus;
}
