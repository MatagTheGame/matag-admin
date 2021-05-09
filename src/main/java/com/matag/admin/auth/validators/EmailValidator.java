package com.matag.admin.auth.validators;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class EmailValidator implements Validator<String> {
  private static final String REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
  private static final Pattern pattern = Pattern.compile(REGEX);
  private static final String EMAIL_IS_INVALID = "Email is invalid.";

  @Override
  public void validate(String email) throws ValidationException {
    if (!pattern.matcher(email).matches() || email.length() > 100) {
      throw new ValidationException(EMAIL_IS_INVALID);
    }
  }
}
