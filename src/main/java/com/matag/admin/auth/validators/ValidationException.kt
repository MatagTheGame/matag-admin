package com.matag.admin.auth.validators;

import com.matag.admin.exception.MatagException;

public class ValidationException extends MatagException {
  public ValidationException(String message) {
    super(message);
  }
}
