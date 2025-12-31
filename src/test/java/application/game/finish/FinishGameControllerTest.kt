package application.game.finish

import application.AbstractApplicationTest
import application.TestUtils
import com.matag.admin.game.game.*
import com.matag.admin.game.join.JoinGameRequest
import com.matag.admin.game.join.JoinGameResponse
import com.matag.admin.game.score.ScoreRepository
import com.matag.admin.game.session.GameSession
import com.matag.admin.game.session.GameSessionRepository
import com.matag.admin.session.MatagSession
import com.matag.adminentities.FinishGameRequest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
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
        Assertions.assertThat<HttpStatusCode?>(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun shouldFinishAGame() {
        // Given
        loginUser(TestUtils.USER_1_SESSION_TOKEN, TestUtils.USER_1_USERNAME)
        val request1 = JoinGameRequest.builder()
            .gameType(GameType.UNLIMITED)
            .playerOptions("player1 options")
            .build()
        val joinGameResponse = postForEntity(
            "/game",
            request1,
            JoinGameResponse::class.java,
            TestUtils.USER_1_SESSION_TOKEN
        )
        val gameId = joinGameResponse.getResponseBody()!!.getGameId()

        loginUser(TestUtils.USER_2_SESSION_TOKEN, TestUtils.USER_2_USERNAME)
        val request2 = JoinGameRequest.builder()
            .gameType(GameType.UNLIMITED)
            .playerOptions("player2 options")
            .build()
        postForEntity(
            "/game",
            request2,
            JoinGameResponse::class.java,
            TestUtils.USER_2_SESSION_TOKEN
        )

        // When
        val finishGameRequest = FinishGameRequest(TestUtils.USER_1_SESSION_TOKEN)
        val response = postForAdmin("/game/" + gameId + "/finish", finishGameRequest)

        // Then
        Assertions.assertThat<HttpStatusCode?>(response.getStatus()).isEqualTo(HttpStatus.OK)
        val game = gameRepository!!.findById(gameId)
        Assertions.assertThat<Game?>(game).isPresent()
        Assertions.assertThat<GameStatusType?>(game.get().getStatus()).isEqualTo(GameStatusType.FINISHED)
        Assertions.assertThat<GameResultType?>(game.get().getResult()).isEqualTo(GameResultType.R1)
        Assertions.assertThat(game.get().getFinishedAt()).isNotNull()

        val gameSessions = StreamSupport.stream<GameSession?>(gameSessionRepository!!.findAll().spliterator(), false)
            .collect(Collectors.toList())
        Assertions.assertThat<GameSession?>(gameSessions).hasSize(2)
        Assertions.assertThat<MatagSession?>(gameSessions.get(0)!!.getSession()).isNull()
        Assertions.assertThat<MatagSession?>(gameSessions.get(1)!!.getSession()).isNull()

        // Check elo score
        Assertions.assertThat(scoreRepository!!.findByUsername(TestUtils.USER_1_USERNAME).getElo()).isEqualTo(1050)
        Assertions.assertThat(scoreRepository.findByUsername(TestUtils.USER_2_USERNAME).getElo()).isEqualTo(950)
    }
}