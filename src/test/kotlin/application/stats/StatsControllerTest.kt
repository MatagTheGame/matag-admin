package application.stats

import application.AbstractApplicationTest
import application.TestUtils
import com.matag.admin.stats.StatsResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import java.time.LocalDateTime

class StatsControllerTest : AbstractApplicationTest() {
    @Test
    fun shouldGetStatsAsUnauthenticatedUser() {
        // When
        val response = getForEntity("/stats", StatsResponse::class.java)

        // Then
        assertThat<HttpStatusCode?>(response.status).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun shouldGetTotalUsers() {
        // When
        val response = getForEntity("/stats", StatsResponse::class.java)

        // Then
        assertThat(response.getResponseBody()?.totaNonGuestlUsers).isEqualTo(2)
    }

    @Test
    fun shouldGetOnlineUsers() {
        // Given
        loginUser(TestUtils.USER_1_SESSION_TOKEN, TestUtils.USER_1_USERNAME)
        setCurrentTime(LocalDateTime.parse("2000-01-01T00:00:00"))
        loginUser(TestUtils.USER_2_SESSION_TOKEN, TestUtils.USER_2_USERNAME)
        setCurrentTime(TEST_START_TIME)
        loginUser(TestUtils.GUEST_SESSION_TOKEN_1, TestUtils.GUEST_USERNAME)
        loginUser(TestUtils.GUEST_SESSION_TOKEN_2, TestUtils.GUEST_USERNAME)

        // When
        val response = getForEntity("/stats", StatsResponse::class.java)

        // Then
        assertThat<String?>(response.getResponseBody()!!.onlineUsers)
            .containsExactlyInAnyOrder("User1", "Guest", "Guest")
    }

    @Test
    fun shouldGetNumOfCards() {
        // When
        val response = getForEntity("/stats", StatsResponse::class.java)

        // Then
        assertThat(response.getResponseBody()!!.totalCards).isGreaterThan(100)
    }

    @Test
    fun shouldGetNumOfSets() {
        // When
        val response = getForEntity("/stats", StatsResponse::class.java)

        // Then
        assertThat(response.getResponseBody()!!.totalSets).isGreaterThan(10)
    }
}