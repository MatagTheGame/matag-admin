package application.auth.register;

import application.AbstractApplicationTest;
import com.matag.admin.auth.register.RegisterRequest;
import com.matag.admin.auth.register.RegisterResponse;
import com.matag.admin.auth.register.VerifyResponse;
import com.matag.admin.exception.ErrorResponse;
import com.matag.admin.game.score.ScoreRepository;
import com.matag.admin.user.MatagUserRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.client.EntityExchangeResult;

import java.time.LocalDateTime;

import static application.TestUtils.PASSWORD;
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
    private ScoreRepository scoreRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Test
    public void shouldReturnInvalidEmail() {
        // Given
        var request = new RegisterRequest("invalidEmail", "username", PASSWORD);

        // When
        var response = postForEntity("/auth/register", request, ErrorResponse.class);

        // Then
        assertErrorRegisterResponse(response, "Email is invalid.");
    }

    @Test
    public void shouldReturnInvalidUsername() {
        // Given
        var request = new RegisterRequest("user1@matag.com", "$£", "xxx");

        // When
        var response = postForEntity("/auth/register", request, ErrorResponse.class);

        // Then
        assertErrorRegisterResponse(response, "Username needs to be between 4 and 25 characters and can contains only letters  number and one of the following characters: [+ - * = _ . @ &].");
    }

    @Test
    public void shouldReturnInvalidPassword() {
        // Given
        var request = new RegisterRequest("user1@matag.com", "username", "xxx");

        // When
        var response = postForEntity("/auth/register", request, ErrorResponse.class);

        // Then
        assertErrorRegisterResponse(response, "Password is invalid (should be at least 4 characters).");
    }

    @Test
    public void shouldReturnEmailAlreadyRegistered() {
        // Given
        var request = new RegisterRequest("user1@matag.com", "username", PASSWORD);

        // When
        var response = postForEntity("/auth/register", request, ErrorResponse.class);

        // Then
        assertErrorRegisterResponse(response, "This email is already registered (use reset password functionality).");
    }

    @Test
    public void shouldReturnUsernameAlreadyRegistered() {
        // Given
        var request = new RegisterRequest("new-user@matag.com", "User1", PASSWORD);

        // When
        var response = postForEntity("/auth/register", request, ErrorResponse.class);

        // Then
        assertErrorRegisterResponse(response, "This username is already registered (please choose a new one).");
    }

    @Test
    @SneakyThrows
    public void registerANewUser() {
        // Given
        var request = new RegisterRequest("new-user@matag.com", "NewUser", PASSWORD);
        var mimeMessage = mockMailSender();

        // When
        var response = postForEntity("/auth/register", request, RegisterResponse.class);

        // Then
        assertSuccessfulRegisterResponse(response);

        var user = loadUser("NewUser");
        assertThat(user.getUsername()).isEqualTo("NewUser");
        assertThat(user.getPassword()).isNotBlank();
        assertThat(user.getEmailAddress()).isEqualTo("new-user@matag.com");
        assertThat(user.getStatus()).isEqualTo(VERIFYING);
        assertThat(user.getCreatedAt()).isNotNull();

        var score = scoreRepository.findByMatagUser(user);
        assertThat(score.getElo()).isEqualTo(1000);

        verify(javaMailSender).send(mimeMessage);
    }

    @Test
    public void verifyAUser() {
        // Given
        String username = "NewUser";
        var request = new RegisterRequest("new-user@matag.com", username, PASSWORD);
        mockMailSender();

        postForEntity("/auth/register", request, RegisterResponse.class);
        var user = loadUser(username);
        var verificationCode = user.getMatagUserVerification().getVerificationCode();

        // When
        var verifyResponse = getForEntity("/auth/verify?username=" + username + "&code=" + verificationCode, VerifyResponse.class);

        // Then
        assertThat(verifyResponse.getStatus()).isEqualTo(OK);
        assertThat(verifyResponse.getResponseBody()).isNotNull();
        assertThat(verifyResponse.getResponseBody().getMessage()).isEqualTo("Your account has been correctly verified. Now you can proceed with logging in.");

        user = loadUser(username);
        assertThat(user.getStatus()).isEqualTo(ACTIVE);
        assertThat(user.getMatagUserVerification().getVerificationCode()).isNull();
        assertThat(user.getMatagUserVerification().getValidUntil()).isNull();
        assertThat(user.getMatagUserVerification().getAttempts()).isEqualTo(0);
    }

    @Test
    public void verifyAnInactiveUser() {
        // Given
        var username = "NewUser";
        var request = new RegisterRequest("new-user@matag.com", username, PASSWORD);
        mockMailSender();

        postForEntity("/auth/register", request, RegisterResponse.class);
        var newUser = loadUser(username);
        var verificationCode = newUser.getMatagUserVerification().getVerificationCode();
        newUser.setStatus(INACTIVE);
        matagUserRepository.save(newUser);

        // When
        var verifyResponse = getForEntity("/auth/verify?username=" + username + "&code=" + verificationCode, ErrorResponse.class);

        // Then
        assertThat(verifyResponse.getStatus()).isEqualTo(BAD_REQUEST);
        assertThat(verifyResponse.getResponseBody()).isNotNull();
        assertThat(verifyResponse.getResponseBody().getError()).isEqualTo("Your account could not be verified. Please send a message to matag.the.game@gmail.com.");

        var user = loadUser(username);
        assertThat(user.getStatus()).isEqualTo(INACTIVE);
    }

    @Test
    public void verifyFailsWithIncorrectVerificationCode() {
        // Given
        var username = "NewUser";
        var request = new RegisterRequest("new-user@matag.com", username, PASSWORD);
        mockMailSender();

        postForEntity("/auth/register", request, RegisterResponse.class);

        // When
        var verifyResponse = getForEntity("/auth/verify?username=" + username + "&code=" + "incorrect-verification-code", ErrorResponse.class);

        // Then
        assertThat(verifyResponse.getStatus()).isEqualTo(BAD_REQUEST);
        assertThat(verifyResponse.getResponseBody()).isNotNull();
        assertThat(verifyResponse.getResponseBody().getError()).isEqualTo("Your account could not be verified. Please send a message to matag.the.game@gmail.com.");

        var user = loadUser(username);
        assertThat(user.getStatus()).isEqualTo(VERIFYING);
        assertThat(user.getMatagUserVerification().getAttempts()).isEqualTo(1);
    }

    @Test
    public void verifyAUserFailsIfTooManyAttempts() {
        // Given
        var username = "NewUser";
        var request = new RegisterRequest("new-user@matag.com", username, PASSWORD);
        mockMailSender();

        postForEntity("/auth/register", request, RegisterResponse.class);
        var user = loadUser(username);
        var verificationCode = user.getMatagUserVerification().getVerificationCode();

        // When
        getForEntity("/auth/verify?username=" + username + "&code=incorrect-code", VerifyResponse.class);
        getForEntity("/auth/verify?username=" + username + "&code=incorrect-code", VerifyResponse.class);
        getForEntity("/auth/verify?username=" + username + "&code=incorrect-code", VerifyResponse.class);
        getForEntity("/auth/verify?username=" + username + "&code=" + verificationCode, VerifyResponse.class);
        getForEntity("/auth/verify?username=" + username + "&code=" + verificationCode, VerifyResponse.class);
        EntityExchangeResult<VerifyResponse> verifyResponse = getForEntity("/auth/verify?username=" + username + "&code=" + verificationCode, VerifyResponse.class);

        // Then
        assertThat(verifyResponse.getStatus()).isEqualTo(BAD_REQUEST);

        user = loadUser(username);
        assertThat(user.getStatus()).isEqualTo(VERIFYING);
        assertThat(user.getMatagUserVerification().getAttempts()).isEqualTo(6);
    }

    @Test
    public void verifyAUserFailsIfAfterValidUntilDate() {
        // Given
        var username = "NewUser";
        var request = new RegisterRequest("new-user@matag.com", username, PASSWORD);
        mockMailSender();

        postForEntity("/auth/register", request, RegisterResponse.class);
        var user = loadUser(username);
        var verificationCode = user.getMatagUserVerification().getVerificationCode();

        setCurrentTime(LocalDateTime.now().plusDays(2));

        // When
        var verifyResponse = getForEntity("/auth/verify?username=" + username + "&code=" + verificationCode, VerifyResponse.class);

        // Then
        assertThat(verifyResponse.getStatus()).isEqualTo(BAD_REQUEST);

        user = loadUser(username);
        assertThat(user.getStatus()).isEqualTo(VERIFYING);
        assertThat(user.getMatagUserVerification().getAttempts()).isEqualTo(1);
    }

    private void assertErrorRegisterResponse(EntityExchangeResult<ErrorResponse> response, String expected) {
        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
        assertThat(response.getResponseBody()).isNotNull();
        assertThat(response.getResponseBody().getError()).isEqualTo(expected);
    }

    private void assertSuccessfulRegisterResponse(EntityExchangeResult<RegisterResponse> response) {
        assertThat(response.getStatus()).isEqualTo(OK);
        assertThat(response.getResponseBody()).isNotNull();
        assertThat(response.getResponseBody().getMessage()).isEqualTo("Registration Successful. Please check your email for a verification code.");
    }

    private MimeMessage mockMailSender() {
        var mimeMessage = Mockito.mock(MimeMessage.class);
        given(javaMailSender.createMimeMessage()).willReturn(mimeMessage);
        return mimeMessage;
    }
}