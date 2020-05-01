package integration.auth.validators;

import com.matag.admin.auth.validators.UsernameValidator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UsernameValidatorTest {
  private final UsernameValidator usernameValidator = new UsernameValidator();

  @Test
  public void validUsernames() {
    assertThat(usernameValidator.isValid("antonio")).isTrue();
    assertThat(usernameValidator.isValid("antonio e antonio")).isTrue();
    assertThat(usernameValidator.isValid("_-.@+=*&")).isTrue();
  }

  @Test
  public void invalidUsernames() {
    assertThat(usernameValidator.isValid("012")).isFalse();
    assertThat(usernameValidator.isValid("012345678901234567890123456")).isFalse();
  }
}