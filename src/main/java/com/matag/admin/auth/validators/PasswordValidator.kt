package com.matag.admin.auth.validators;

import org.springframework.stereotype.Component;

@Component
public class PasswordValidator implements Validator<String> {
  private static final String PASSWORD_IS_INVALID = "Password is invalid (should be at least 4 characters).";

  @Override
  public void validate(String password) throws ValidationException {
    if (password.length() < 4) {
      throw new ValidationException(PASSWORD_IS_INVALID);
    }
  }
}
