package integration.auth.validators;

import com.matag.admin.auth.validators.PasswordValidator;
import com.matag.admin.auth.validators.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PasswordValidatorTest {
  private final PasswordValidator passwordValidator = new PasswordValidator();

  @Test
  public void validPassword() {
    passwordValidator.validate("valid");
  }

  @Test
  public void tooShortPassword() {
    Assertions.assertThrows(ValidationException.class, () ->
      passwordValidator.validate("1")
    );
  }
}