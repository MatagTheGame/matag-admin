package application.session;

import application.AbstractApplicationTest;
import com.matag.admin.session.MatagSessionRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static application.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class AuthSessionFilterTest extends AbstractApplicationTest {
  @Autowired
  private MatagSessionRepository matagSessionRepository;

  @Test
  public void shouldGrantAccessToAResourceToLoggedInUsers() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, USER_1_USERNAME);

    // When
    var response = restTemplate.getForEntity("/path/to/a/resource", String.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void shouldNotGrantAccessToAResourceToNonLoggedInUsers() {
    // When
    var response = restTemplate.getForEntity("/path/to/a/resource", String.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
  }

  @Test
  public void shouldNotGrantAccessToAResourceIfUserSessionIsExpired() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    setCurrentTime(TEST_START_TIME.plusHours(1).plusMinutes(1));

    // When
    var response = restTemplate.getForEntity("/path/to/a/resource", String.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
  }

  @Test
  public void shouldExtendTheSessionAfterHalfOfItsLife() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    setCurrentTime(TEST_START_TIME.plusMinutes(45));

    // When
    restTemplate.getForEntity("/path/to/a/resource", String.class);

    // Then
    assertThat(matagSessionRepository.findBySessionId(USER_1_SESSION_TOKEN).isPresent()).isTrue();
    assertThat(matagSessionRepository.findBySessionId(USER_1_SESSION_TOKEN).get().getValidUntil()).isEqualTo(TEST_START_TIME.plusHours(1).plusMinutes(45));
  }
}