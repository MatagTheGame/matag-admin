package com.matag.admin.auth.validators;

public class ValidationException extends RuntimeException {
  public ValidationException(String message) {
    super(message);
  }
}
