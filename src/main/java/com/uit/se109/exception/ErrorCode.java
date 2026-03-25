package com.uit.se109.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  UNCATEGORIZED_EXCEPTION(2000, "An uncategorized exception occurred.", 500),
  RESOURCE_NOT_FOUND(2001, "The requested resource was not found.", 404),
  VALIDATION_ERROR(2002, "Validation failed for the request.", 400),
  INVALID_REFRESH_TOKEN(3001, "The provided refresh token is invalid.", 401),
  REFRESH_TOKEN_EXPIRED(3002, "The provided refresh token has expired.", 401),
  USER_NOT_FOUND(3003, "User not found.", 404),
  INVALID_CREDENTIAL(3004, "Invalid username or password.", 401),
  ;
  private final Integer code;
  private final String message;
  private final int httpStatus;
}
