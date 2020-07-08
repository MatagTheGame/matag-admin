package application.auth.login;

import application.AbstractApplicationTest;
import com.matag.admin.auth.login.LoginRequest;
import com.matag.admin.auth.login.LoginResponse;
import com.matag.admin.user.profile.CurrentUserProfileDto;
import org.junit.Test;

import static application.TestUtils.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class LoginControllerTest extends AbstractApplicationTest {
  @Test
  public void shouldReturnInvalidPassword() {
    // Given
    var request = new LoginRequest("user1@matag.com", "xxx");

    // When
    var response = restTemplate.postForEntity("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().error()).isEqualTo("Password is invalid (should be at least 4 characters).");
  }

  @Test
  public void shouldLoginAUserViaEmail() {
    // Given
    var request = new LoginRequest("user1@matag.com", PASSWORD);

    // When
    var response = restTemplate.postForObject("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.token()).isNotBlank();
    assertThat(response.profile()).isEqualTo(CurrentUserProfileDto.builder()
      .username("User1")
      .type("USER")
      .build());
  }

  @Test
  public void shouldLoginAUserViaUsername() {
    // Given
    var request = new LoginRequest("User1", PASSWORD);

    // When
    var response = restTemplate.postForObject("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.token()).isNotBlank();
    assertThat(response.profile()).isEqualTo(CurrentUserProfileDto.builder()
      .username("User1")
      .type("USER")
      .build());
  }

  @Test
  public void shouldNotLoginANonExistingUser() {
    // Given
    var request = new LoginRequest("non-existing-user@matag.com", PASSWORD);

    // When
    var response = restTemplate.postForEntity("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(UNAUTHORIZED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().error()).isEqualTo("Email/Username or password are not correct.");
  }

  @Test
  public void shouldNotLoginWithWrongPassword() {
    // Given
    var request = new LoginRequest("user1@matag.com", "wrong-password");

    // When
    var response = restTemplate.postForEntity("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(UNAUTHORIZED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().error()).isEqualTo("Email/Username or password are not correct.");
  }

  @Test
  public void shouldNotLoginNotActiveUser() {
    // Given
    var request = new LoginRequest("inactiveUser@matag.com", PASSWORD);

    // When
    var response = restTemplate.postForEntity("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(UNAUTHORIZED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().error()).isEqualTo("Account is not active.");
  }
}