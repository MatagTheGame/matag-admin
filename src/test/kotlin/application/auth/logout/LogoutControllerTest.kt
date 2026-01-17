package application.auth.logout

import application.AbstractApplicationTest
import application.TestUtils
import com.matag.admin.session.MatagSessionRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

class LogoutControllerTest : AbstractApplicationTest() {
    @Test
    fun shouldLogoutAUser() {
        // Given
        loginUser(TestUtils.USER_1_SESSION_TOKEN, TestUtils.USER_1_USERNAME)

        // When
        val logoutResponse = getForEntity("/auth/logout", String::class.java, TestUtils.USER_1_SESSION_TOKEN)

        // Then
        assertThat(logoutResponse.status).isEqualTo(HttpStatus.OK)
        assertThat(matagSessionRepository.count()).isEqualTo(0)
    }

    @Test
    fun shouldLogoutANonLoggedInUser() {
        // When
        val logoutResponse = getForEntity("/auth/logout", String::class.java)

        // Then
        assertThat(logoutResponse.status).isEqualTo(HttpStatus.OK)
    }
}