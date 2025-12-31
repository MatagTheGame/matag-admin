package application.auth.login

import application.AbstractApplicationTest
import application.TestUtils
import application.TestUtils.PASSWORD
import com.matag.admin.auth.login.LoginRequest
import com.matag.admin.auth.login.LoginResponse
import com.matag.admin.user.profile.CurrentUserProfileDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.UNAUTHORIZED

class LoginControllerTest : AbstractApplicationTest() {
    @Test
    fun shouldReturnInvalidPassword() {
        // Given
        val request = LoginRequest("user1@matag.com", "xxx")

        // When
        val response = postForEntity("/auth/login", request, LoginResponse::class.java)

        // Then
        assertThat(response.status).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(response.getResponseBody()?.getError())
            .isEqualTo("Password is invalid (should be at least 4 characters).")
    }

    @Test
    fun shouldLoginAUserViaEmail() {
        // Given
        val request = LoginRequest("user1@matag.com", PASSWORD)

        // When
        val response = postForEntity("/auth/login", request, LoginResponse::class.java)

        // Then
        assertThat(response.getResponseBody()?.getToken()).isNotBlank()
        assertThat(response.getResponseBody()?.getProfile()).isEqualTo(
            CurrentUserProfileDto.builder()
                .username("User1")
                .type("USER")
                .build()
        )
    }

    @Test
    fun shouldLoginAUserViaUsername() {
        // Given
        val request = LoginRequest("User1", PASSWORD)

        // When
        val response = postForEntity("/auth/login", request, LoginResponse::class.java)

        // Then
        assertThat(response.getResponseBody()?.getToken()).isNotBlank()
        assertThat(response.getResponseBody()?.getProfile()).isEqualTo(
            CurrentUserProfileDto.builder()
                .username("User1")
                .type("USER")
                .build()
        )
    }

    @Test
    fun shouldNotLoginANonExistingUser() {
        // Given
        val request = LoginRequest("non-existing-user@matag.com", PASSWORD)

        // When
        val response = postForEntity("/auth/login", request, LoginResponse::class.java)

        // Then
        assertThat(response.status).isEqualTo(UNAUTHORIZED)
        assertThat(response.getResponseBody()?.getError())
            .isEqualTo("Email/Username or password are not correct.")
    }

    @Test
    fun shouldNotLoginWithWrongPassword() {
        // Given
        val request = LoginRequest("user1@matag.com", "wrong-password")

        // When
        val response = postForEntity("/auth/login", request, LoginResponse::class.java)

        // Then
        assertThat(response.status).isEqualTo(UNAUTHORIZED)
        assertThat(response.getResponseBody()!!.getError())
            .isEqualTo("Email/Username or password are not correct.")
    }

    @Test
    fun shouldNotLoginNotActiveUser() {
        // Given
        val request = LoginRequest("inactiveUser@matag.com", PASSWORD)

        // When
        val response = postForEntity("/auth/login", request, LoginResponse::class.java)

        // Then
        assertThat(response.status).isEqualTo(UNAUTHORIZED)
        assertThat(response.getResponseBody()?.getError()).isEqualTo("Account is not active.")
    }

    @Test
    fun shouldNotCreateTwoSessionsForSameUser() {
        // Given a user already logged in once
        val request = LoginRequest("User1", PASSWORD)
        var response = postForEntity("/auth/login", request, LoginResponse::class.java)
        val firstToken = response.getResponseBody()?.getToken()

        // When user login twice
        response = postForEntity("/auth/login", request, LoginResponse::class.java)
        val secondToken = response.getResponseBody()?.getToken()

        // Then
        assertThat(firstToken).isNotNull().isEqualTo(secondToken)
    }
}