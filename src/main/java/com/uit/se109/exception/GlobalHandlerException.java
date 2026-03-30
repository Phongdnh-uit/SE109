package com.uit.se109.exception;

import com.uit.se109.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalHandlerException {

  @ExceptionHandler(AppException.class)
  public ResponseEntity<ApiResponse<Void>> handleAppException(AppException ex) {
    ApiResponse<Void> response = new ApiResponse<>();
    response.setCode(ex.getErrorCode().getCode());
    response.setMessage(ex.getErrorCode().getMessage());
    response.setErrors(ex.getDetails());
    log.error("AppException occurred: {}", ex.getMessage(), ex);
    return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(response);
  }

  @ExceptionHandler({Exception.class, RuntimeException.class})
  public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
    log.error("An unexpected error occurred: ", ex);
    ApiResponse<Void> response = new ApiResponse<>();
    response.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
    response.setMessage("An unexpected error occurred.");
    return ResponseEntity.status(500).body(response);
  }
}
