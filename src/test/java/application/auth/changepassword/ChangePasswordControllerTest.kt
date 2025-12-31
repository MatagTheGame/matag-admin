package application.auth.changepassword

import application.AbstractApplicationTest
import application.TestUtils
import com.matag.admin.auth.changepassword.ChangePasswordRequest
import com.matag.admin.auth.changepassword.ChangePasswordResponse
import com.matag.admin.auth.login.LoginRequest
import com.matag.admin.auth.login.LoginResponse
import com.matag.admin.exception.ErrorResponse
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

class ChangePasswordControllerTest : AbstractApplicationTest() {
    @Test
    fun shouldReturnInvalidOldPassword() {
        // Given
        loginUser(TestUtils.USER_1_SESSION_TOKEN, TestUtils.USER_1_USERNAME)
        val request = ChangePasswordRequest("wrong-password", "new-password")

        // When
        val response = postForEntity(
            "/auth/change-password",
            request,
            ErrorResponse::class.java,
            TestUtils.USER_1_SESSION_TOKEN
        )

        // Then
        Assertions.assertThat<HttpStatusCode?>(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(response.getResponseBody()!!.getError()).isEqualTo("Your password wasn't matched.")
    }

    @Test
    fun shouldReturnInvalidNewPassword() {
        // Given
        loginUser(TestUtils.USER_1_SESSION_TOKEN, TestUtils.USER_1_USERNAME)
        val request = ChangePasswordRequest("password", "xxx")

        // When
        val response = postForEntity(
            "/auth/change-password",
            request,
            ErrorResponse::class.java,
            TestUtils.USER_1_SESSION_TOKEN
        )

        // Then
        Assertions.assertThat<HttpStatusCode?>(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(response.getResponseBody()!!.getError())
            .isEqualTo("The new password you chose is invalid: Password is invalid (should be at least 4 characters).")
    }

    @Test
    fun shouldChangeThePassword() {
        // Given
        loginUser(TestUtils.USER_1_SESSION_TOKEN, TestUtils.USER_1_USERNAME)
        val request = ChangePasswordRequest("password", "new-password")

        // When
        val response = postForEntity(
            "/auth/change-password",
            request,
            ChangePasswordResponse::class.java,
            TestUtils.USER_1_SESSION_TOKEN
        )

        // Then
        Assertions.assertThat<HttpStatusCode?>(response.getStatus()).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.getResponseBody()!!.getMessage()).isEqualTo("Password changed.")

        // And user can login with the new password
        val loginRequest = LoginRequest("user1@matag.com", "new-password")
        val loginResponse = postForEntity(
            "/auth/login",
            loginRequest,
            LoginResponse::class.java,
            TestUtils.USER_1_SESSION_TOKEN
        )
        Assertions.assertThat(loginResponse.getResponseBody()!!.getToken()).isNotBlank()
    }
}