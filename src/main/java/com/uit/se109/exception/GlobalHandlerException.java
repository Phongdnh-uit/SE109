package com.uit.se109.exception;

import com.uit.se109.dto.ApiResponse;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleValidationException(
      MethodArgumentNotValidException ex) {
    ApiResponse<Void> response = new ApiResponse<>();
    response.setCode(ErrorCode.VALIDATION_ERROR.getCode());
    response.setMessage(ErrorCode.VALIDATION_ERROR.getMessage());
    response.setErrors(
        ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(ext -> ext.getField(), ext -> ext.getDefaultMessage())));
    log.error("Validation error: {}", response.getErrors(), ex);
    return ResponseEntity.status(400).body(response);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolationException(
      DataIntegrityViolationException ex) {
    log.error("Data integrity violation: ", ex);
    ApiResponse<Void> response = new ApiResponse<>();
    response.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
    response.setMessage(
        "Data integrity violation occurred. Possible duplicate entry or constraint violation.");
    return ResponseEntity.status(409).body(response);
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
