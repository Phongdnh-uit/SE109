package com.uit.se109.exception;

import com.uit.se109.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {

  @ExceptionHandler(AppException.class)
  public ResponseEntity<ApiResponse<Void>> handleAppException(AppException ex) {
    ApiResponse<Void> response = new ApiResponse<>();
    response.setCode(ex.getErrorCode().getCode());
    response.setMessage(ex.getErrorCode().getMessage());
    response.setErrors(ex.getDetails());
    return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(response);
  }
}
