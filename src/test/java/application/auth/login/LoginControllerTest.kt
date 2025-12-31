package application.auth.login;

import static application.TestUtils.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.matag.admin.auth.login.LoginRequest;
import com.matag.admin.auth.login.LoginResponse;
import com.matag.admin.user.profile.CurrentUserProfileDto;

import application.AbstractApplicationTest;
import org.junit.jupiter.api.Test;

public class LoginControllerTest extends AbstractApplicationTest {
  @Test
  public void shouldReturnInvalidPassword() {
    // Given
    var request = new LoginRequest("user1@matag.com", "xxx");

    // When
    var response = postForEntity("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
    assertThat(response.getResponseBody()).isNotNull();
    assertThat(response.getResponseBody().getError()).isEqualTo("Password is invalid (should be at least 4 characters).");
  }

  @Test
  public void shouldLoginAUserViaEmail() {
    // Given
    var request = new LoginRequest("user1@matag.com", PASSWORD);

    // When
    var response = postForEntity("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getResponseBody().getToken()).isNotBlank();
    assertThat(response.getResponseBody().getProfile()).isEqualTo(CurrentUserProfileDto.builder()
      .username("User1")
      .type("USER")
      .build());
  }

  @Test
  public void shouldLoginAUserViaUsername() {
    // Given
    var request = new LoginRequest("User1", PASSWORD);

    // When
    var response = postForEntity("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getResponseBody().getToken()).isNotBlank();
    assertThat(response.getResponseBody().getProfile()).isEqualTo(CurrentUserProfileDto.builder()
      .username("User1")
      .type("USER")
      .build());
  }

  @Test
  public void shouldNotLoginANonExistingUser() {
    // Given
    var request = new LoginRequest("non-existing-user@matag.com", PASSWORD);

    // When
    var response = postForEntity("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getStatus()).isEqualTo(UNAUTHORIZED);
    assertThat(response.getResponseBody()).isNotNull();
    assertThat(response.getResponseBody().getError()).isEqualTo("Email/Username or password are not correct.");
  }

  @Test
  public void shouldNotLoginWithWrongPassword() {
    // Given
    var request = new LoginRequest("user1@matag.com", "wrong-password");

    // When
    var response = postForEntity("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getStatus()).isEqualTo(UNAUTHORIZED);
    assertThat(response.getResponseBody()).isNotNull();
    assertThat(response.getResponseBody().getError()).isEqualTo("Email/Username or password are not correct.");
  }

  @Test
  public void shouldNotLoginNotActiveUser() {
    // Given
    var request = new LoginRequest("inactiveUser@matag.com", PASSWORD);

    // When
    var response = postForEntity("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getStatus()).isEqualTo(UNAUTHORIZED);
    assertThat(response.getResponseBody()).isNotNull();
    assertThat(response.getResponseBody().getError()).isEqualTo("Account is not active.");
  }

  @Test
  public void shouldNotCreateTwoSessionsForSameUser() {
    // Given a user already logged in once
    var request = new LoginRequest("User1", PASSWORD);
    var response = postForEntity("/auth/login", request, LoginResponse.class);
    String firstToken = response.getResponseBody().getToken();

    // When user login twice
    response = postForEntity("/auth/login", request, LoginResponse.class);
    String secondToken = response.getResponseBody().getToken();

    // Then
    assertThat(firstToken).isEqualTo(secondToken);
  }
}