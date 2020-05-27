package application.auth.register;

import application.AbstractApplicationTest;
import com.matag.admin.auth.register.RegisterRequest;
import com.matag.admin.auth.register.RegisterResponse;
import com.matag.admin.auth.register.VerifyResponse;
import com.matag.admin.user.MatagUser;
import com.matag.admin.user.MatagUserRepository;
import lombok.SneakyThrows;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;

import java.time.LocalDateTime;

import static com.matag.admin.user.MatagUserStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class RegisterControllerTest extends AbstractApplicationTest {
  @Autowired
  private MatagUserRepository matagUserRepository;

  @Autowired
  private JavaMailSender javaMailSender;

  @Test
  public void shouldReturnInvalidEmail() {
    // Given
    RegisterRequest request = new RegisterRequest("invalidEmail", "username", "password");

    // When
    ResponseEntity<RegisterResponse> response = restTemplate.postForEntity("/auth/register", request, RegisterResponse.class);

    // Then
    assertErrorRegisterResponse(response, "Email is invalid.");
  }

  @Test
  public void shouldReturnInvalidUsername() {
    // Given
    RegisterRequest request = new RegisterRequest("user1@matag.com", "$£", "xxx");

    // When
    ResponseEntity<RegisterResponse> response = restTemplate.postForEntity("/auth/register", request, RegisterResponse.class);

    // Then
    assertErrorRegisterResponse(response, "Username needs to be between 4 and 25 characters and can contains only letters  number and one of the following characters: [+ - * = _ . @ &].");
  }

  @Test
  public void shouldReturnInvalidPassword() {
    // Given
    RegisterRequest request = new RegisterRequest("user1@matag.com", "username", "xxx");

    // When
    ResponseEntity<RegisterResponse> response = restTemplate.postForEntity("/auth/register", request, RegisterResponse.class);

    // Then
    assertErrorRegisterResponse(response, "Password is invalid.");
  }

  @Test
  public void shouldReturnEmailAlreadyRegistered() {
    // Given
    RegisterRequest request = new RegisterRequest("user1@matag.com", "username", "password");

    // When
    ResponseEntity<RegisterResponse> response = restTemplate.postForEntity("/auth/register", request, RegisterResponse.class);

    // Then
    assertErrorRegisterResponse(response, "This email is already registered (use reset password functionality).");
  }

  @Test
  public void shouldReturnUsernameAlreadyRegistered() {
    // Given
    RegisterRequest request = new RegisterRequest("new-user@matag.com", "User1", "password");

    // When
    ResponseEntity<RegisterResponse> response = restTemplate.postForEntity("/auth/register", request, RegisterResponse.class);

    // Then
    assertErrorRegisterResponse(response, "This username is already registered (please choose a new one).");
  }

  @Test
  @SneakyThrows
  public void registerANewUser() {
    // Given
    RegisterRequest request = new RegisterRequest("new-user@matag.com", "NewUser", "password");
    MimeMessage mimeMessage = mockMailSender();

    // When
    ResponseEntity<RegisterResponse> response = restTemplate.postForEntity("/auth/register", request, RegisterResponse.class);

    // Then
    assertSuccessfulRegisterResponse(response);

    MatagUser user = loadUser("NewUser");
    assertThat(user.getUsername()).isEqualTo("NewUser");
    assertThat(user.getPassword()).isNotBlank();
    assertThat(user.getEmailAddress()).isEqualTo("new-user@matag.com");
    assertThat(user.getStatus()).isEqualTo(VERIFYING);
    assertThat(user.getCreatedAt()).isNotNull();

    verify(javaMailSender).send(mimeMessage);
  }

  @Test
  public void verifyAUser() {
    // Given
    String username = "NewUser";
    RegisterRequest request = new RegisterRequest("new-user@matag.com", username, "password");
    mockMailSender();

    restTemplate.postForEntity("/auth/register", request, RegisterResponse.class);
    MatagUser user = loadUser(username);
    String verificationCode = user.getMatagUserVerification().getVerificationCode();

    // When
    ResponseEntity<VerifyResponse> verifyResponse = restTemplate.getForEntity("/auth/verify?username=" + username + "&code=" + verificationCode, VerifyResponse.class);

    // Then
    assertThat(verifyResponse.getStatusCode()).isEqualTo(OK);
    assertThat(verifyResponse.getBody()).isNotNull();
    assertThat(verifyResponse.getBody().getMessage()).isEqualTo("Your account has been correctly verified. Now you can proceed with logging in.");

    user = loadUser(username);
    assertThat(user.getStatus()).isEqualTo(ACTIVE);
    assertThat(user.getMatagUserVerification().getVerificationCode()).isNull();
    assertThat(user.getMatagUserVerification().getValidUntil()).isNull();
    assertThat(user.getMatagUserVerification().getAttempts()).isEqualTo(0);
  }

  @Test
  public void verifyAnInactiveUser() {
    // Given
    String username = "NewUser";
    RegisterRequest request = new RegisterRequest("new-user@matag.com", username, "password");
    mockMailSender();

    restTemplate.postForEntity("/auth/register", request, RegisterResponse.class);
    MatagUser newUser = loadUser(username);
    String verificationCode = newUser.getMatagUserVerification().getVerificationCode();
    newUser.setStatus(INACTIVE);
    matagUserRepository.save(newUser);

    // When
    ResponseEntity<VerifyResponse> verifyResponse = restTemplate.getForEntity("/auth/verify?username=" + username + "&code=" + verificationCode, VerifyResponse.class);

    // Then
    assertThat(verifyResponse.getStatusCode()).isEqualTo(BAD_REQUEST);
    assertThat(verifyResponse.getBody()).isNotNull();
    assertThat(verifyResponse.getBody().getError()).isEqualTo("Your account could not be verified. Please send a message to matag.the.game@gmail.com.");

    MatagUser user = loadUser(username);
    assertThat(user.getStatus()).isEqualTo(INACTIVE);
  }

  @Test
  public void verifyFailsWithIncorrectVerificationCode() {
    // Given
    String username = "NewUser";
    RegisterRequest request = new RegisterRequest("new-user@matag.com", username, "password");
    mockMailSender();

    restTemplate.postForEntity("/auth/register", request, RegisterResponse.class);

    // When
    ResponseEntity<VerifyResponse> verifyResponse = restTemplate.getForEntity("/auth/verify?username=" + username + "&code=" + "incorrect-verification-code", VerifyResponse.class);

    // Then
    assertThat(verifyResponse.getStatusCode()).isEqualTo(BAD_REQUEST);
    assertThat(verifyResponse.getBody()).isNotNull();
    assertThat(verifyResponse.getBody().getError()).isEqualTo("Your account could not be verified. Please send a message to matag.the.game@gmail.com.");

    MatagUser user = loadUser(username);
    assertThat(user.getStatus()).isEqualTo(VERIFYING);
    assertThat(user.getMatagUserVerification().getAttempts()).isEqualTo(1);
  }

  @Test
  public void verifyAUserFailsIfTooManyAttempts() {
    // Given
    String username = "NewUser";
    RegisterRequest request = new RegisterRequest("new-user@matag.com", username, "password");
    mockMailSender();

    restTemplate.postForEntity("/auth/register", request, RegisterResponse.class);
    MatagUser user = loadUser(username);
    String verificationCode = user.getMatagUserVerification().getVerificationCode();

    // When
    restTemplate.getForEntity("/auth/verify?username=" + username + "&code=incorrect-code", VerifyResponse.class);
    restTemplate.getForEntity("/auth/verify?username=" + username + "&code=incorrect-code", VerifyResponse.class);
    restTemplate.getForEntity("/auth/verify?username=" + username + "&code=incorrect-code", VerifyResponse.class);
    restTemplate.getForEntity("/auth/verify?username=" + username + "&code=" + verificationCode, VerifyResponse.class);
    restTemplate.getForEntity("/auth/verify?username=" + username + "&code=" + verificationCode, VerifyResponse.class);
    ResponseEntity<VerifyResponse> verifyResponse = restTemplate.getForEntity("/auth/verify?username=" + username + "&code=" + verificationCode, VerifyResponse.class);

    // Then
    assertThat(verifyResponse.getStatusCode()).isEqualTo(BAD_REQUEST);

    user = loadUser(username);
    assertThat(user.getStatus()).isEqualTo(VERIFYING);
    assertThat(user.getMatagUserVerification().getAttempts()).isEqualTo(6);
  }

  @Test
  public void verifyAUserFailsIfAfterValidUntilDate() {
    // Given
    String username = "NewUser";
    RegisterRequest request = new RegisterRequest("new-user@matag.com", username, "password");
    mockMailSender();

    restTemplate.postForEntity("/auth/register", request, RegisterResponse.class);
    MatagUser user = loadUser(username);
    String verificationCode = user.getMatagUserVerification().getVerificationCode();

    setCurrentTime(LocalDateTime.now().plusDays(2));

    // When
    ResponseEntity<VerifyResponse> verifyResponse = restTemplate.getForEntity("/auth/verify?username=" + username + "&code=" + verificationCode, VerifyResponse.class);

    // Then
    assertThat(verifyResponse.getStatusCode()).isEqualTo(BAD_REQUEST);

    user = loadUser(username);
    assertThat(user.getStatus()).isEqualTo(VERIFYING);
    assertThat(user.getMatagUserVerification().getAttempts()).isEqualTo(1);
  }

  private void assertErrorRegisterResponse(ResponseEntity<RegisterResponse> response, String expected) {
    assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getMessage()).isNull();
    assertThat(response.getBody().getError()).isEqualTo(expected);
  }

  private void assertSuccessfulRegisterResponse(ResponseEntity<RegisterResponse> response) {
    assertThat(response.getStatusCode()).isEqualTo(OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getError()).isNull();
    assertThat(response.getBody().getMessage()).isEqualTo("Registration Successful. Please check your email for a verification code.");
  }

  private MimeMessage mockMailSender() {
    MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
    given(javaMailSender.createMimeMessage()).willReturn(mimeMessage);
    return mimeMessage;
  }

}