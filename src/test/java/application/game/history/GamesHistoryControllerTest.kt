package application.game.history

import application.AbstractApplicationTest
import application.TestUtils
import com.matag.admin.game.game.*
import com.matag.admin.game.history.GameHistory
import com.matag.admin.game.history.GamesHistoryResponse
import com.matag.admin.game.session.GameSession
import com.matag.admin.game.session.GameSessionRepository
import com.matag.admin.user.MatagUser
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import java.time.LocalDateTime

class GamesHistoryControllerTest : AbstractApplicationTest() {
    @Test
    fun returnsForbiddenForNonLoggedInUsers() {
        // When
        val response = getForEntity("/game/history", GamesHistoryResponse::class.java)

        // Then
        assertThat(response.status).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun retrieveGameHistory() {
        // Given
        loginUser(TestUtils.USER_1_SESSION_TOKEN, TestUtils.USER_1_USERNAME)
        val user1 = loadUser(TestUtils.USER_1_USERNAME)
        val user2 = loadUser(TestUtils.USER_2_USERNAME)

        val game1 = createGame(GameResultType.R1)
        createGameSession(game1, user1)
        createGameSession(game1, user2)

        val game2 = createGame(GameResultType.R2)
        createGameSession(game2, user1)
        createGameSession(game2, user2)

        val game3 = createGame(GameResultType.RX)
        createGameSession(game3, user2)
        createGameSession(game3, user1)

        // When
        val gamesHistoryResponse = getForEntity(
            "/game/history",
            GamesHistoryResponse::class.java,
            TestUtils.USER_1_SESSION_TOKEN
        )

        // Then
        assertThat(gamesHistoryResponse.getResponseBody()?.getGamesHistory()).hasSize(3)
        assertThat(gamesHistoryResponse.getResponseBody()?.getGamesHistory()?.get(0))
            .usingRecursiveComparison().ignoringFields("gameId").isEqualTo(
                GameHistory.builder()
                    .startedTime(LocalDateTime.now(clock))
                    .finishedTime(LocalDateTime.now(clock))
                    .type(GameType.UNLIMITED)
                    .result(GameUserResultType.WIN)
                    .player1Name(TestUtils.USER_1_USERNAME)
                    .player1Options("User1 options")
                    .player2Name(TestUtils.USER_2_USERNAME)
                    .player2Options("User2 options")
                    .build()
            )
        assertThat(gamesHistoryResponse.getResponseBody()?.getGamesHistory()?.get(1))
            .usingRecursiveComparison().ignoringFields("gameId").isEqualTo(
                GameHistory.builder()
                    .startedTime(LocalDateTime.now(clock))
                    .finishedTime(LocalDateTime.now(clock))
                    .type(GameType.UNLIMITED)
                    .result(GameUserResultType.LOST)
                    .player1Name(TestUtils.USER_1_USERNAME)
                    .player1Options("User1 options")
                    .player2Name(TestUtils.USER_2_USERNAME)
                    .player2Options("User2 options")
                    .build()
            )
        assertThat(gamesHistoryResponse.getResponseBody()?.getGamesHistory()?.get(2))
            .usingRecursiveComparison().ignoringFields("gameId").isEqualTo(
                GameHistory.builder()
                    .startedTime(LocalDateTime.now(clock))
                    .finishedTime(LocalDateTime.now(clock))
                    .type(GameType.UNLIMITED)
                    .result(GameUserResultType.DRAW)
                    .player1Name(TestUtils.USER_2_USERNAME)
                    .player1Options("User2 options")
                    .player2Name(TestUtils.USER_1_USERNAME)
                    .player2Options("User1 options")
                    .build()
            )
    }

    private fun createGame(result: GameResultType?): Game {
        val game = Game.builder()
            .createdAt(LocalDateTime.now(clock))
            .type(GameType.UNLIMITED)
            .status(GameStatusType.FINISHED)
            .result(result)
            .finishedAt(LocalDateTime.now(clock))
            .build()
        gameRepository.save(game)
        return game
    }

    private fun createGameSession(game: Game?, user: MatagUser) {
        val gameSession = GameSession.builder()
            .game(game)
            .player(user)
            .playerOptions(user.username + " options")
            .build()
        gameSessionRepository.save(gameSession)
    }
}