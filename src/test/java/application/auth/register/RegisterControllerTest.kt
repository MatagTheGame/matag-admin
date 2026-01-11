package application.auth.register

import application.AbstractApplicationTest
import application.TestUtils
import com.matag.admin.auth.register.RegisterRequest
import com.matag.admin.auth.register.RegisterResponse
import com.matag.admin.auth.register.VerifyResponse
import com.matag.admin.exception.ErrorResponse
import com.matag.admin.game.score.ScoreRepository
import com.matag.admin.user.MatagUser
import com.matag.admin.user.MatagUserRepository
import com.matag.admin.user.MatagUserStatus
import jakarta.mail.internet.MimeMessage
import lombok.SneakyThrows
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.test.web.servlet.client.EntityExchangeResult
import java.time.LocalDateTime

class RegisterControllerTest(
    @param:Autowired private val javaMailSender: JavaMailSender
) : AbstractApplicationTest() {

    @Test
    fun shouldReturnInvalidEmail() {
        // Given
        val request = RegisterRequest("invalidEmail", "username", TestUtils.PASSWORD)

        // When
        val response = postForEntity("/auth/register", request, ErrorResponse::class.java)

        // Then
        assertErrorRegisterResponse(response, "Email is invalid.")
    }

    @Test
    fun shouldReturnInvalidUsername() {
        // Given
        val request = RegisterRequest("user1@matag.com", "$£", "xxx")

        // When
        val response = postForEntity("/auth/register", request, ErrorResponse::class.java)

        // Then
        assertErrorRegisterResponse(
            response,
            "Username needs to be between 4 and 25 characters and can contains only letters  number and one of the following characters: [+ - * = _ . @ &]."
        )
    }

    @Test
    fun shouldReturnInvalidPassword() {
        // Given
        val request = RegisterRequest("user1@matag.com", "username", "xxx")

        // When
        val response = postForEntity("/auth/register", request, ErrorResponse::class.java)

        // Then
        assertErrorRegisterResponse(response, "Password is invalid (should be at least 4 characters).")
    }

    @Test
    fun shouldReturnEmailAlreadyRegistered() {
        // Given
        val request = RegisterRequest("user1@matag.com", "username", TestUtils.PASSWORD)

        // When
        val response = postForEntity("/auth/register", request, ErrorResponse::class.java)

        // Then
        assertErrorRegisterResponse(response, "This email is already registered.")
    }

    @Test
    fun shouldReturnUsernameAlreadyRegistered() {
        // Given
        val request = RegisterRequest("new-user@matag.com", "User1", TestUtils.PASSWORD)

        // When
        val response = postForEntity("/auth/register", request, ErrorResponse::class.java)

        // Then
        assertErrorRegisterResponse(response, "This username is already registered (please choose a new one).")
    }

    @Test
    @SneakyThrows
    fun registerANewUser() {
        // Given
        val request = RegisterRequest("new-user@matag.com", "NewUser", TestUtils.PASSWORD)
        val mimeMessage = mockMailSender()

        // When
        val response = postForEntity("/auth/register", request, RegisterResponse::class.java)

        // Then
        assertSuccessfulRegisterResponse(response)

        val user = loadUser("NewUser")
        assertThat(user.username).isEqualTo("NewUser")
        assertThat(user.password).isNotBlank()
        assertThat(user.emailAddress).isEqualTo("new-user@matag.com")
        assertThat(user.status).isEqualTo(MatagUserStatus.VERIFYING)
        assertThat(user.createdAt).isNotNull()

        val score = scoreRepository.findByMatagUser(user)
        assertThat(score.elo).isEqualTo(1000)

        Mockito.verify(javaMailSender).send(mimeMessage)
    }

    @Test
    fun verifyAUser() {
        // Given
        val username = "NewUser"
        val request = RegisterRequest("new-user@matag.com", username, TestUtils.PASSWORD)
        mockMailSender()

        postForEntity("/auth/register", request, RegisterResponse::class.java)
        var user = loadUser(username)
        val verificationCode = user.matagUserVerification?.verificationCode

        // When
        val verifyResponse = getForEntity("/auth/verify?username=$username&code=$verificationCode", VerifyResponse::class.java
        )

        // Then
        assertThat(verifyResponse.status).isEqualTo(HttpStatus.OK)
        assertThat(verifyResponse.getResponseBody()?.message)
            .isEqualTo("Your account has been correctly verified. Now you can proceed with logging in.")

        user = loadUser(username)
        assertThat(user.status).isEqualTo(MatagUserStatus.ACTIVE)
        assertThat(user.matagUserVerification?.verificationCode).isNull()
        assertThat(user.matagUserVerification?.validUntil).isNull()
        assertThat(user.matagUserVerification?.attempts).isEqualTo(0)
    }

    @Test
    fun verifyAnInactiveUser() {
        // Given
        val username = "NewUser"
        val request = RegisterRequest("new-user@matag.com", username, TestUtils.PASSWORD)
        mockMailSender()

        postForEntity("/auth/register", request, RegisterResponse::class.java)
        val newUser = loadUser(username)
        val verificationCode = newUser.matagUserVerification?.verificationCode
        newUser.status = MatagUserStatus.INACTIVE
        matagUserRepository.save(newUser)

        // When
        val verifyResponse = getForEntity("/auth/verify?username=$username&code=$verificationCode", ErrorResponse::class.java)

        // Then
        assertThat(verifyResponse.status).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(verifyResponse.getResponseBody()).isNotNull()
        assertThat(verifyResponse.getResponseBody()!!.getError())
            .isEqualTo("Your account could not be verified. Please send a message to matag.the.game@gmail.com.")

        val user = loadUser(username)
        assertThat(user.status).isEqualTo(MatagUserStatus.INACTIVE)
    }

    @Test
    fun verifyFailsWithIncorrectVerificationCode() {
        // Given
        val username = "NewUser"
        val request = RegisterRequest("new-user@matag.com", username, TestUtils.PASSWORD)
        mockMailSender()

        postForEntity("/auth/register", request, RegisterResponse::class.java)

        // When
        val verifyResponse = getForEntity("/auth/verify?username=$username&code=incorrect-verification-code", ErrorResponse::class.java)

        // Then
        assertThat(verifyResponse.status).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(verifyResponse.getResponseBody()?.getError())
            .isEqualTo("Your account could not be verified. Please send a message to matag.the.game@gmail.com.")

        val user = loadUser(username)
        assertThat(user.status).isEqualTo(MatagUserStatus.VERIFYING)
        assertThat(user.matagUserVerification?.attempts).isEqualTo(1)
    }

    @Test
    fun verifyAUserFailsIfTooManyAttempts() {
        // Given
        val username = "NewUser"
        val request = RegisterRequest("new-user@matag.com", username, TestUtils.PASSWORD)
        mockMailSender()

        postForEntity("/auth/register", request, RegisterResponse::class.java)
        var user = loadUser(username)
        val verificationCode = user.matagUserVerification?.verificationCode

        // When
        getForEntity("/auth/verify?username=$username&code=incorrect-code", VerifyResponse::class.java)
        getForEntity("/auth/verify?username=$username&code=incorrect-code", VerifyResponse::class.java)
        getForEntity("/auth/verify?username=$username&code=incorrect-code", VerifyResponse::class.java)
        getForEntity("/auth/verify?username=$username&code=$verificationCode", VerifyResponse::class.java)
        getForEntity("/auth/verify?username=$username&code=$verificationCode", VerifyResponse::class.java)
        val verifyResponse = getForEntity("/auth/verify?username=$username&code=$verificationCode", VerifyResponse::class.java)

        // Then
        assertThat(verifyResponse.status).isEqualTo(HttpStatus.BAD_REQUEST)

        user = loadUser(username)
        assertThat(user.status).isEqualTo(MatagUserStatus.VERIFYING)
        assertThat(user.matagUserVerification?.attempts).isEqualTo(6)
    }

    @Test
    fun verifyAUserFailsIfAfterValidUntilDate() {
        // Given
        val username = "NewUser"
        val request = RegisterRequest("new-user@matag.com", username, TestUtils.PASSWORD)
        mockMailSender()

        postForEntity("/auth/register", request, RegisterResponse::class.java)
        var user = loadUser(username)
        val verificationCode = user.matagUserVerification?.verificationCode

        setCurrentTime(LocalDateTime.now().plusDays(2))

        // When
        val verifyResponse = getForEntity("/auth/verify?username=$username&code=$verificationCode", VerifyResponse::class.java)

        // Then
        assertThat(verifyResponse.status).isEqualTo(HttpStatus.BAD_REQUEST)

        user = loadUser(username)
        assertThat(user.status).isEqualTo(MatagUserStatus.VERIFYING)
        assertThat(user.matagUserVerification?.attempts).isEqualTo(1)
    }

    private fun assertErrorRegisterResponse(response: EntityExchangeResult<ErrorResponse>, expected: String) {
        assertThat(response.status).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(response.getResponseBody()?.getError()).isEqualTo(expected)
    }

    private fun assertSuccessfulRegisterResponse(response: EntityExchangeResult<RegisterResponse>) {
        assertThat(response.status).isEqualTo(HttpStatus.OK)
        assertThat(response.getResponseBody()?.message)
            .isEqualTo("Registration Successful. Please check your email for a verification code.")
    }

    private fun mockMailSender(): MimeMessage {
        val mimeMessage = Mockito.mock<MimeMessage>(MimeMessage::class.java)
        BDDMockito.given(javaMailSender.createMimeMessage()).willReturn(mimeMessage)
        return mimeMessage
    }
}