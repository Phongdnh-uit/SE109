package com.uit.se109.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  UNCATEGORIZED_EXCEPTION("UNCATEGORIZED_EXCEPTION", "An uncategorized exception occurred.", 500),
  RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", "The requested resource was not found.", 404);
  private final String code;
  private final String message;
  private final int httpStatus;
}
