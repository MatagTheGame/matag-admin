package com.matag.admin.auth.validators;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class UsernameValidator implements Validator<String> {
  private static final String REGEX = "^[A-Za-z0-9 +\\-*=_.@&]{4,25}$";
  private static final Pattern pattern = Pattern.compile(REGEX);
  private static final String USERNAME_IS_INVALID = "Username needs to be between 4 and 25 characters and can contains only letters  number and one of the following characters: [+ - * = _ . @ &].";

  @Override
  public void validate(String username) throws ValidationException {
    if (!pattern.matcher(username).matches()) {
      throw new ValidationException(USERNAME_IS_INVALID);
    }
  }
}
