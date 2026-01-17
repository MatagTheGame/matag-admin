package application.auth.login

import application.AbstractApplicationTest
import application.TestUtils.PASSWORD
import com.matag.admin.auth.login.CurrentUserProfileDto
import com.matag.admin.auth.login.LoginRequest
import com.matag.admin.auth.login.LoginResponse
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
        assertThat(response.getResponseBody()?.error)
            .isEqualTo("Password is invalid (should be at least 4 characters).")
    }

    @Test
    fun shouldLoginAUserViaEmail() {
        // Given
        val request = LoginRequest("user1@matag.com", PASSWORD)

        // When
        val response = postForEntity("/auth/login", request, LoginResponse::class.java)

        // Then
        assertThat(response.getResponseBody()?.token).isNotBlank()
        assertThat(response.getResponseBody()?.profile).isEqualTo(
            CurrentUserProfileDto(
                username = "User1",
                type = "USER"
            )
        )
    }

    @Test
    fun shouldLoginAUserViaUsername() {
        // Given
        val request = LoginRequest("User1", PASSWORD)

        // When
        val response = postForEntity("/auth/login", request, LoginResponse::class.java)

        // Then
        assertThat(response.getResponseBody()?.token).isNotBlank()
        assertThat(response.getResponseBody()?.profile).isEqualTo(
            CurrentUserProfileDto(
                username = "User1",
                type = "USER"
            )
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
        assertThat(response.getResponseBody()?.error)
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
        assertThat(response.getResponseBody()!!.error)
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
        assertThat(response.getResponseBody()?.error).isEqualTo("Account is not active.")
    }

    @Test
    fun shouldNotCreateTwoSessionsForSameUser() {
        // Given a user already logged in once
        val request = LoginRequest("User1", PASSWORD)
        var response = postForEntity("/auth/login", request, LoginResponse::class.java)
        val firstToken = response.getResponseBody()?.token

        // When user login twice
        response = postForEntity("/auth/login", request, LoginResponse::class.java)
        val secondToken = response.getResponseBody()?.token

        // Then
        assertThat(firstToken).isNotNull().isEqualTo(secondToken)
    }
}