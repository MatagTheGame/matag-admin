package com.matag.admin.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.matag.admin.auth.register.RegisterController;

@ControllerAdvice
public class MatagExceptionHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

  @ExceptionHandler(MatagException.class)
  public ResponseEntity<ErrorResponse> handle(MatagException e) {
    LOGGER.error("Error {}", e.getMessage());
    return ResponseEntity
      .status(BAD_REQUEST)
      .body(ErrorResponse.builder()
        .error(e.getMessage())
        .build());
  }

  @ExceptionHandler(InsufficientAuthenticationException.class)
  public ResponseEntity<ErrorResponse> handle(InsufficientAuthenticationException e) {
    LOGGER.error("Error {}", e.getMessage(), e);
    return ResponseEntity
      .status(UNAUTHORIZED)
      .body(ErrorResponse.builder()
        .error(e.getMessage())
        .build());
  }
}
