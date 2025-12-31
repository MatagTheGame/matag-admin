package application.session

import application.AbstractApplicationTest
import application.TestUtils
import com.matag.admin.session.MatagSessionRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.test.web.servlet.client.ExchangeResult

class AuthSessionFilterTest : AbstractApplicationTest() {
    @Test
    fun shouldGrantAccessToAResourceToLoggedInUsers() {
        // Given
        loginUser(TestUtils.USER_1_SESSION_TOKEN, TestUtils.USER_1_USERNAME)

        // When
        val response = getForEntity("/path/to/a/resource", String::class.java, TestUtils.USER_1_SESSION_TOKEN)

        // Then
        Assertions.assertThat<HttpStatusCode?>(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun shouldNotGrantAccessToAResourceToNonLoggedInUsers() {
        // When
        val response = getForEntity("/path/to/a/resource", String::class.java)

        // Then
        Assertions.assertThat<HttpStatusCode?>(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun shouldNotGrantAccessToAResourceIfUserSessionIsExpired() {
        // Given
        loginUser(TestUtils.USER_1_SESSION_TOKEN, TestUtils.USER_1_USERNAME)
        setCurrentTime(TEST_START_TIME.plusHours(1).plusMinutes(1))

        // When
        val response = getForEntity("/path/to/a/resource", String::class.java, TestUtils.USER_1_SESSION_TOKEN)

        // Then
        Assertions.assertThat<HttpStatusCode?>(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun shouldExtendTheSessionAfterHalfOfItsLife() {
        // Given
        loginUser(TestUtils.USER_1_SESSION_TOKEN, TestUtils.USER_1_USERNAME)
        setCurrentTime(TEST_START_TIME.plusMinutes(45))

        // When
        val response: ExchangeResult =
            getForEntity("/ui/admin", String::class.java, TestUtils.USER_1_SESSION_TOKEN)

        // Then
        Assertions.assertThat<HttpStatusCode?>(response.getStatus()).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(matagSessionRepository!!.findBySessionId(TestUtils.USER_1_SESSION_TOKEN).isPresent())
            .isTrue()
        Assertions.assertThat(
            matagSessionRepository.findBySessionId(TestUtils.USER_1_SESSION_TOKEN).get().getValidUntil()
        ).isEqualTo(TEST_START_TIME.plusHours(1).plusMinutes(45))
    }
}