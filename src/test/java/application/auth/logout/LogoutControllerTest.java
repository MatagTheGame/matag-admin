package application.auth.logout;

import static application.TestUtils.USER_1_SESSION_TOKEN;
import static application.TestUtils.USER_1_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.matag.admin.session.MatagSessionRepository;

import application.AbstractApplicationTest;

public class LogoutControllerTest extends AbstractApplicationTest {
  @Autowired
  private MatagSessionRepository matagSessionRepository;

  @Test
  public void shouldLogoutAUser() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, USER_1_USERNAME);

    // When
    var logoutResponse = restTemplate.getForEntity("/auth/logout", String.class);

    // Then
    assertThat(logoutResponse.getStatusCode()).isEqualTo(OK);
    assertThat(matagSessionRepository.count()).isEqualTo(0);
  }

  @Test
  public void shouldLogoutANonLoggedInUser() {
    // When
    var logoutResponse = restTemplate.getForEntity("/auth/logout", String.class);

    // Then
    assertThat(logoutResponse.getStatusCode()).isEqualTo(OK);
  }
}