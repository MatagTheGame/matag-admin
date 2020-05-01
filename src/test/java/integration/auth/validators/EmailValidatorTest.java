package integration.auth.validators;

import com.matag.admin.auth.validators.EmailValidator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailValidatorTest {
  private final EmailValidator emailValidator = new EmailValidator();

  @Test
  public void invalidEmail() {
    assertThat(emailValidator.isValid("antonio")).isFalse();
  }

  @Test
  public void validEmail() {
    assertThat(emailValidator.isValid("antonio@mtg.com")).isTrue();
  }

  @Test
  public void tooLongEmail() {
    assertThat(emailValidator.isValid("antonio123".repeat(10) + "@mtg.com")).isFalse();
  }
}