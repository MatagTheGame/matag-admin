package application.sets

import application.AbstractApplicationTest
import application.TestUtils.USER_1_SESSION_TOKEN
import application.TestUtils.USER_1_USERNAME
import com.matag.admin.auth.changepassword.ChangePasswordRequest
import com.matag.admin.auth.changepassword.ChangePasswordResponse
import com.matag.admin.auth.login.LoginRequest
import com.matag.admin.auth.login.LoginResponse
import com.matag.admin.exception.ErrorResponse
import com.matag.admin.sets.MtgSet
import com.matag.admin.sets.MtgSetsResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.OK

class SetsControllerTest : AbstractApplicationTest() {
    @Test
    fun shouldReturnInvalidOldPassword() {
        // When
        val response = getForEntity(
            "/sets",
            MtgSetsResponse::class.java
        )

        // Then
        assertThat(response.status).isEqualTo(OK)
        assertThat(response.responseBody?.sets).hasSizeGreaterThan(5)
        assertThat(response.responseBody?.sets).contains(
            MtgSet(code = "AER", name = "Aether Revolt")
        )
    }
}