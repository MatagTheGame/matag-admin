package application.session

import application.AbstractApplicationTest
import application.TestUtils
import application.TestUtils.USER_1_SESSION_TOKEN
import application.TestUtils.USER_1_USERNAME
import com.matag.admin.session.MatagSessionRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatusCode
import org.springframework.test.web.servlet.client.ExchangeResult

class AuthSessionFilterTest : AbstractApplicationTest() {
    @Test
    fun shouldGrantAccessToAResourceToLoggedInUsers() {
        // Given
        loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME)

        // When
        val response = getForEntity("/game/history", String::class.java, USER_1_SESSION_TOKEN)

        // Then
        assertThat(response.status).isEqualTo(OK)
    }

    @Test
    fun shouldNotGrantAccessToAResourceToNonLoggedInUsers() {
        // When
        val response = getForEntity("/game/history", String::class.java)

        // Then
        assertThat(response.status).isEqualTo(FORBIDDEN)
    }

    @Test
    fun shouldNotGrantAccessToAResourceIfUserSessionIsExpired() {
        // Given
        loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME)
        setCurrentTime(TEST_START_TIME.plusHours(1).plusMinutes(1))

        // When
        val response = getForEntity("/game/history", String::class.java, USER_1_SESSION_TOKEN)

        // Then
        assertThat(response.status).isEqualTo(FORBIDDEN)
    }

    @Test
    fun shouldExtendTheSessionAfterHalfOfItsLife() {
        // Given
        loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME)
        setCurrentTime(TEST_START_TIME.plusMinutes(45))

        // When
        val response = getForEntity("/ui/admin", String::class.java, USER_1_SESSION_TOKEN)

        // Then
        assertThat(response.status).isEqualTo(OK)
        assertThat(matagSessionRepository.findBySessionId(USER_1_SESSION_TOKEN)).isPresent()
        assertThat(matagSessionRepository.findBySessionId(USER_1_SESSION_TOKEN).get().validUntil)
            .isEqualTo(TEST_START_TIME.plusHours(1).plusMinutes(45))
    }
}