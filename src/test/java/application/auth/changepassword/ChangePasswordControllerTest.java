package application.auth.changepassword;

import static application.TestUtils.USER_1_SESSION_TOKEN;
import static application.TestUtils.USER_1_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import org.junit.Test;

import com.matag.admin.auth.changepassword.ChangePasswordRequest;
import com.matag.admin.auth.changepassword.ChangePasswordResponse;
import com.matag.admin.auth.login.LoginRequest;
import com.matag.admin.auth.login.LoginResponse;
import com.matag.admin.exception.ErrorResponse;

import application.AbstractApplicationTest;

public class ChangePasswordControllerTest extends AbstractApplicationTest {
  @Test
  public void shouldReturnInvalidOldPassword() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    var request = new ChangePasswordRequest("wrong-password", "new-password");

    // When
    var response = restTemplate.postForEntity("/auth/change-password", request, ErrorResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getError()).isEqualTo("Your password wasn't matched.");
  }

  @Test
  public void shouldReturnInvalidNewPassword() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    var request = new ChangePasswordRequest("password", "xxx");

    // When
    var response = restTemplate.postForEntity("/auth/change-password", request, ErrorResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getError()).isEqualTo("The new password you chose is invalid: Password is invalid (should be at least 4 characters).");
  }

  @Test
  public void shouldChangeThePassword() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    var request = new ChangePasswordRequest("password", "new-password");

    // When
    var response = restTemplate.postForEntity("/auth/change-password", request, ChangePasswordResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getMessage()).isEqualTo("Password changed.");

    // And user can login with the new password
    LoginRequest loginRequest = new LoginRequest("user1@matag.com", "new-password");
    LoginResponse loginResponse = restTemplate.postForObject("/auth/login", loginRequest, LoginResponse.class);
    assertThat(loginResponse.getToken()).isNotBlank();
  }
}