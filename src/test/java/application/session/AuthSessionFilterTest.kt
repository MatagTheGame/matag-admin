package application.session;

import static application.TestUtils.USER_1_SESSION_TOKEN;
import static application.TestUtils.USER_1_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.matag.admin.session.MatagSessionRepository;

import application.AbstractApplicationTest;
import org.springframework.test.web.servlet.client.ExchangeResult;

public class AuthSessionFilterTest extends AbstractApplicationTest {
  @Autowired
  private MatagSessionRepository matagSessionRepository;

  @Test
  public void shouldGrantAccessToAResourceToLoggedInUsers() {
    // Given
    loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME);

    // When
    var response = getForEntity("/path/to/a/resource", String.class, USER_1_SESSION_TOKEN);

    // Then
    assertThat(response.getStatus()).isEqualTo(NOT_FOUND);
  }

  @Test
  public void shouldNotGrantAccessToAResourceToNonLoggedInUsers() {
    // When
    var response = getForEntity("/path/to/a/resource", String.class);

    // Then
    assertThat(response.getStatus()).isEqualTo(FORBIDDEN);
  }

  @Test
  public void shouldNotGrantAccessToAResourceIfUserSessionIsExpired() {
    // Given
    loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    setCurrentTime(TEST_START_TIME.plusHours(1).plusMinutes(1));

    // When
    var response = getForEntity("/path/to/a/resource", String.class, USER_1_SESSION_TOKEN);

    // Then
    assertThat(response.getStatus()).isEqualTo(FORBIDDEN);
  }

  @Test
  public void shouldExtendTheSessionAfterHalfOfItsLife() {
    // Given
    loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    setCurrentTime(TEST_START_TIME.plusMinutes(45));

    // When
      ExchangeResult response = getForEntity("/ui/admin", String.class, USER_1_SESSION_TOKEN);

      // Then
      assertThat(response.getStatus()).isEqualTo(OK);
    assertThat(matagSessionRepository.findBySessionId(USER_1_SESSION_TOKEN).isPresent()).isTrue();
    assertThat(matagSessionRepository.findBySessionId(USER_1_SESSION_TOKEN).get().getValidUntil()).isEqualTo(TEST_START_TIME.plusHours(1).plusMinutes(45));
  }
}