package application.game.findactive

import application.AbstractApplicationTest
import application.TestUtils
import application.TestUtils.USER_1_SESSION_TOKEN
import application.TestUtils.USER_1_USERNAME
import com.matag.admin.game.findactive.ActiveGameResponse
import com.matag.admin.game.game.GameType
import com.matag.admin.game.join.JoinGameRequest
import com.matag.admin.game.join.JoinGameResponse
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

class FindActiveGameControllerTest : AbstractApplicationTest() {
    @Test
    fun shouldFindNoGames() {
        // Given
        loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME)

        // When
        val response = getForEntity("/game", ActiveGameResponse::class.java)

        // Then
        assertThat(response.getResponseBody()?.gameId).isNull()
    }

    @Test
    fun shouldFindAnActiveGame() {
        // Given
        loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME)

        val request = JoinGameRequest(
            gameType = GameType.UNLIMITED,
            playerOptions = "player1 options"
        )
        postForEntity("/game", request, JoinGameResponse::class.java, USER_1_SESSION_TOKEN)

        // When
        val response = getForEntity("/game", ActiveGameResponse::class.java, USER_1_SESSION_TOKEN)

        // Then
        assertThat(response.status).isEqualTo(HttpStatus.OK)
        assertThat(response.getResponseBody()?.gameId).isGreaterThan(0)
        assertThat(response.getResponseBody()?.createdAt).isNotNull()
        assertThat(response.getResponseBody()?.playerName).isEqualTo("User1")
        assertThat(response.getResponseBody()?.playerOptions).isEqualTo("player1 options")
        assertThat(response.getResponseBody()?.opponentName).isNull()
        assertThat(response.getResponseBody()?.opponentOptions).isNull()
    }
}