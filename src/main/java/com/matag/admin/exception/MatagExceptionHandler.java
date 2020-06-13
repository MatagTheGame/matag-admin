package com.matag.admin.exception;

import com.matag.admin.auth.register.RegisterController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class MatagExceptionHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(MatagException.class)
  public ResponseEntity<ErrorResponse> handle(MatagException e) {
    LOGGER.error("Error {}", e.getMessage());
    return ResponseEntity
      .status(BAD_REQUEST)
      .body(ErrorResponse.builder()
        .error(e.getMessage())
        .build());
  }
}
