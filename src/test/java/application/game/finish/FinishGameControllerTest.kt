package application.game.finish

import application.AbstractApplicationTest
import application.TestUtils
import com.matag.admin.game.game.*
import com.matag.admin.game.join.JoinGameRequest
import com.matag.admin.game.join.JoinGameResponse
import com.matag.admin.game.finish.FinishGameRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.util.stream.Collectors
import java.util.stream.StreamSupport

class FinishGameControllerTest : AbstractApplicationTest() {
    @Test
    fun requiresAdminAuthentication() {
        // Given
        loginUser(TestUtils.USER_1_SESSION_TOKEN, TestUtils.USER_1_USERNAME)

        // When
        val finishGameRequest = FinishGameRequest(TestUtils.USER_1_SESSION_TOKEN)
        val response =
            postForEntity("/game/1/finish", finishGameRequest, Any::class.java, TestUtils.USER_1_SESSION_TOKEN)

        // Then
        assertThat(response.status).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun shouldFinishAGame() {
        // Given
        loginUser(TestUtils.USER_1_SESSION_TOKEN, TestUtils.USER_1_USERNAME)
        val request1 = JoinGameRequest(
            gameType = GameType.UNLIMITED,
            playerOptions = "player1 options",
        )
        val joinGameResponse = postForEntity(
            "/game",
            request1,
            JoinGameResponse::class.java,
            TestUtils.USER_1_SESSION_TOKEN
        )
        val gameId = requireNotNull(joinGameResponse.getResponseBody()?.gameId)

        loginUser(TestUtils.USER_2_SESSION_TOKEN, TestUtils.USER_2_USERNAME)
        val request2 = JoinGameRequest(
            gameType = GameType.UNLIMITED,
            playerOptions = "player2 options"
        )
        postForEntity(
            "/game",
            request2,
            JoinGameResponse::class.java,
            TestUtils.USER_2_SESSION_TOKEN
        )

        // When
        val finishGameRequest = FinishGameRequest(TestUtils.USER_1_SESSION_TOKEN)
        val response = postForAdmin("/game/$gameId/finish", finishGameRequest)

        // Then
        assertThat(response.status).isEqualTo(HttpStatus.OK)
        val game = gameRepository.findById(gameId)
        assertThat(game).isPresent()
        assertThat(game.get().status).isEqualTo(GameStatus.FINISHED)
        assertThat(game.get().result).isEqualTo(GameResultType.R1)
        assertThat(game.get().finishedAt).isNotNull()

        val gameSessions = StreamSupport.stream(gameSessionRepository.findAll().spliterator(), false)
            .collect(Collectors.toList())
        assertThat(gameSessions).hasSize(2)
        assertThat(gameSessions[0].session).isNull()
        assertThat(gameSessions[1].session).isNull()

        // Check elo score
        assertThat(scoreRepository.findByUsername(TestUtils.USER_1_USERNAME).getElo()).isEqualTo(1050)
        assertThat(scoreRepository.findByUsername(TestUtils.USER_2_USERNAME).getElo()).isEqualTo(950)
    }
}