package application.auth.register;

import application.AbstractApplicationTest;
import com.matag.admin.auth.register.RegisterRequest;
import com.matag.admin.auth.register.RegisterResponse;
import com.matag.admin.user.MatagUser;
import com.matag.admin.user.MatagUserRepository;
import lombok.SneakyThrows;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;
import java.util.Optional;

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
    assertErrorResponse(response, "Email is invalid.");
  }

  @Test
  public void shouldReturnInvalidUsername() {
    // Given
    RegisterRequest request = new RegisterRequest("user1@matag.com", "$£", "xxx");

    // When
    ResponseEntity<RegisterResponse> response = restTemplate.postForEntity("/auth/register", request, RegisterResponse.class);

    // Then
    assertErrorResponse(response, "Username needs to be between 4 and 25 characters and can contains only letters  number and one of the following characters: [+ - * = _ . @ &].");
  }

  @Test
  public void shouldReturnInvalidPassword() {
    // Given
    RegisterRequest request = new RegisterRequest("user1@matag.com", "username", "xxx");

    // When
    ResponseEntity<RegisterResponse> response = restTemplate.postForEntity("/auth/register", request, RegisterResponse.class);

    // Then
    assertErrorResponse(response, "Password is invalid.");
  }

  @Test
  public void shouldReturnEmailAlreadyRegistered() {
    // Given
    RegisterRequest request = new RegisterRequest("user1@matag.com", "username", "password");

    // When
    ResponseEntity<RegisterResponse> response = restTemplate.postForEntity("/auth/register", request, RegisterResponse.class);

    // Then
    assertErrorResponse(response, "This email is already registered (use reset password functionality).");
  }

  @Test
  public void shouldReturnUsernameAlreadyRegistered() {
    // Given
    RegisterRequest request = new RegisterRequest("new-user@matag.com", "User1", "password");

    // When
    ResponseEntity<RegisterResponse> response = restTemplate.postForEntity("/auth/register", request, RegisterResponse.class);

    // Then
    assertErrorResponse(response, "This username is already registered (please choose a new one).");
  }

  @Test
  @SneakyThrows
  public void registerANewUser() {
    // Given
    RegisterRequest request = new RegisterRequest("new-user@matag.com", "NewUser", "password");

    MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
    given(javaMailSender.createMimeMessage()).willReturn(mimeMessage);

    // When
    ResponseEntity<RegisterResponse> response = restTemplate.postForEntity("/auth/register", request, RegisterResponse.class);

    // Then
    assertSuccessfulResponse(response);

    Optional<MatagUser> user = matagUserRepository.findByUsername("NewUser");
    assertThat(user).isPresent();
    assertThat(user.get().getUsername()).isEqualTo("NewUser");
    assertThat(user.get().getPassword()).isNotBlank();
    assertThat(user.get().getEmailAddress()).isEqualTo("new-user@matag.com");
    assertThat(user.get().getCreatedAt()).isNotNull();

    verify(javaMailSender).send(mimeMessage);
  }

  private void assertErrorResponse(ResponseEntity<RegisterResponse> response, String expected) {
    assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getMessage()).isNull();
    assertThat(response.getBody().getError()).isEqualTo(expected);
  }

  private void assertSuccessfulResponse(ResponseEntity<RegisterResponse> response) {
    assertThat(response.getStatusCode()).isEqualTo(OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getError()).isNull();
    assertThat(response.getBody().getMessage()).isEqualTo("Registration Successful. Please check your email for a verification code.");
  }
}