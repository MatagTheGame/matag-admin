package application.game.cancel

import application.AbstractApplicationTest
import application.TestUtils
import com.matag.admin.game.game.*
import com.matag.admin.game.join.JoinGameRequest
import com.matag.admin.game.join.JoinGameResponse
import com.matag.admin.game.session.GameSession
import com.matag.admin.session.MatagSession
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.util.stream.Collectors
import java.util.stream.StreamSupport

class CancelGameControllerTest : AbstractApplicationTest() {
    @Test
    fun shouldCancelAGameWhereNobodyJoined() {
        // Given
        loginUser(TestUtils.USER_1_SESSION_TOKEN, TestUtils.USER_1_USERNAME)
        val request = JoinGameRequest(
            gameType = GameType.UNLIMITED,
            playerOptions = "player1 options",
        )
        val joinGameResponse = postForEntity(
            "/game",
            request,
            JoinGameResponse::class.java,
            TestUtils.USER_1_SESSION_TOKEN
        )
        val gameId = joinGameResponse.getResponseBody()?.gameId

        // When
        val response = delete("/game/$gameId", TestUtils.USER_1_SESSION_TOKEN)

        // Then
        assertThat(response.status).isEqualTo(HttpStatus.OK)
        assertThat(gameSessionRepository.findAll()).hasSize(0)
        assertThat(gameRepository.findAll()).hasSize(0)
    }

    @Test
    fun shouldCancelAGameWhereSomebodyJoined() {
        // Given
        loginUser(TestUtils.USER_1_SESSION_TOKEN, TestUtils.USER_1_USERNAME)
        val request1 = JoinGameRequest(
            gameType = GameType.UNLIMITED,
            playerOptions = "player1 options"
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
        loginUser(TestUtils.USER_1_SESSION_TOKEN, TestUtils.USER_1_USERNAME)
        val response = delete("/game/$gameId", TestUtils.USER_1_SESSION_TOKEN)

        // Then
        assertThat(response.status).isEqualTo(HttpStatus.OK)
        val game = gameRepository.findById(gameId)
        assertThat(game).isPresent()
        assertThat(game.get().status).isEqualTo(GameStatus.FINISHED)
        assertThat(game.get().result).isEqualTo(GameResultType.R2)
        assertThat(game.get().finishedAt).isNotNull()

        val gameSessions = StreamSupport.stream<GameSession?>(gameSessionRepository.findAll().spliterator(), false)
            .collect(Collectors.toList())
        assertThat<GameSession?>(gameSessions).hasSize(2)
        assertThat<MatagSession?>(gameSessions[0].session).isNull()
        assertThat<MatagSession?>(gameSessions[1].session).isNull()
    }
}