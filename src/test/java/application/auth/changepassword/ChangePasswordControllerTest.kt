package application.auth.changepassword

import application.AbstractApplicationTest
import application.TestUtils.USER_1_SESSION_TOKEN
import application.TestUtils.USER_1_USERNAME
import com.matag.admin.auth.changepassword.ChangePasswordRequest
import com.matag.admin.auth.changepassword.ChangePasswordResponse
import com.matag.admin.auth.login.LoginRequest
import com.matag.admin.auth.login.LoginResponse
import com.matag.admin.exception.ErrorResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.OK

class ChangePasswordControllerTest : AbstractApplicationTest() {
    @Test
    fun shouldReturnInvalidOldPassword() {
        // Given
        loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME)
        val request = ChangePasswordRequest("wrong-password", "new-password")

        // When
        val response = postForEntity(
            "/auth/change-password",
            request,
            ErrorResponse::class.java,
            USER_1_SESSION_TOKEN
        )

        // Then
        assertThat(response.status).isEqualTo(BAD_REQUEST)
        assertThat(response.getResponseBody()?.getError()).isEqualTo("Your password wasn't matched.")
    }

    @Test
    fun shouldReturnInvalidNewPassword() {
        // Given
        loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME)
        val request = ChangePasswordRequest("password", "xxx")

        // When
        val response = postForEntity(
            "/auth/change-password",
            request,
            ErrorResponse::class.java,
            USER_1_SESSION_TOKEN
        )

        // Then
        assertThat(response.status).isEqualTo(BAD_REQUEST)
        assertThat(response.getResponseBody()?.getError())
            .isEqualTo("The new password you chose is invalid: Password is invalid (should be at least 4 characters).")
    }

    @Test
    fun shouldChangeThePassword() {
        // Given
        loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME)
        val request = ChangePasswordRequest("password", "new-password")

        // When
        val response = postForEntity(
            "/auth/change-password",
            request,
            ChangePasswordResponse::class.java,
            USER_1_SESSION_TOKEN
        )

        // Then
        assertThat(response.status).isEqualTo(OK)
        assertThat(response.getResponseBody()?.message).isEqualTo("Password changed.")

        // And user can login with the new password
        val loginRequest = LoginRequest("user1@matag.com", "new-password")
        val loginResponse = postForEntity(
            "/auth/login",
            loginRequest,
            LoginResponse::class.java,
            USER_1_SESSION_TOKEN
        )
        assertThat(loginResponse.getResponseBody()?.token).isNotBlank()
    }
}