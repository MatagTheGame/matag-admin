package integration.auth.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.matag.admin.auth.validators.UsernameValidator;
import com.matag.admin.auth.validators.ValidationException;

public class UsernameValidatorTest {
  private final UsernameValidator usernameValidator = new UsernameValidator();

  @Test
  public void validUsernames() {
    usernameValidator.validate("antonio");
    usernameValidator.validate("antonio e antonio");
    usernameValidator.validate("_-.@+=*&");
  }

  @Test
  public void tooShortUsername() {
    Assertions.assertThrows(ValidationException.class, () ->
      usernameValidator.validate("012")
    );
  }

  @Test
  public void notSureWhy() {
    Assertions.assertThrows(ValidationException.class, () ->
      usernameValidator.validate("012345678901234567890123456")
    );
  }
}