package integration.auth.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.matag.admin.auth.validators.EmailValidator;
import com.matag.admin.auth.validators.ValidationException;

public class EmailValidatorTest {
  private final EmailValidator emailValidator = new EmailValidator();

  @Test
  public void validEmail() {
    emailValidator.validate("antonio@mtg.com");
  }

  @Test
  public void invalidEmail() {
    Assertions.assertThrows(ValidationException.class, () ->
      emailValidator.validate("antonio")
    );
  }

  @Test
  public void tooLongEmail() {
    Assertions.assertThrows(ValidationException.class, () ->
      emailValidator.validate("antonio123".repeat(10) + "@mtg.com")
    );
  }
}