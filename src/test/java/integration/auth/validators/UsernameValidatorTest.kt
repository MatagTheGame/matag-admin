package integration.auth.validators;

import com.matag.admin.auth.validators.UsernameValidator;
import com.matag.admin.auth.validators.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
    assertThrows(ValidationException.class, () ->
      usernameValidator.validate("AB")
    );
  }

  @Test
  public void notSureWhy() {
    assertThrows(ValidationException.class, () ->
      usernameValidator.validate("012345678901234567890123456")
    );
  }
}