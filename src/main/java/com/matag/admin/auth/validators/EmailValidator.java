package com.matag.admin.auth.validators;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class EmailValidator {
  private final static String REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
  private final static Pattern pattern = Pattern.compile(REGEX);

  public boolean isValid(String email) {
    return pattern.matcher(email).matches() && email.length() <= 100;
  }
}
