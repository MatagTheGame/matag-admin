package application.game.join

import application.AbstractApplicationTest
import application.TestUtils
import com.matag.admin.game.game.Game
import com.matag.admin.game.game.GameRepository
import com.matag.admin.game.game.GameStatusType
import com.matag.admin.game.game.GameType
import com.matag.admin.game.join.JoinGameRequest
import com.matag.admin.game.join.JoinGameResponse
import com.matag.admin.game.session.GameSession
import com.matag.admin.game.session.GameSessionRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class JoinGameControllerTest : AbstractApplicationTest() {
    @Test
    fun shouldCreateAGame() {
        // Given
        loginUser(TestUtils.USER_1_SESSION_TOKEN, TestUtils.USER_1_USERNAME)
        val request = JoinGameRequest.builder()
            .gameType(GameType.UNLIMITED)
            .playerOptions("player1 options")
            .build()

        // When
        val response = postForEntity(
            "/game",
            request,
            JoinGameResponse::class.java,
            TestUtils.USER_1_SESSION_TOKEN
        )

        // Then
        Assertions.assertThat(response.getResponseBody()!!.getGameId()).isGreaterThan(0)

        val games = gameRepository!!.findAll()
        Assertions.assertThat<Game?>(games).hasSize(1)
        val game = games.iterator().next()
        Assertions.assertThat(game.getId()).isEqualTo(response.getResponseBody()!!.getGameId())
        Assertions.assertThat<GameType?>(game.getType()).isEqualTo(GameType.UNLIMITED)
        Assertions.assertThat<GameStatusType?>(game.getStatus()).isEqualTo(GameStatusType.STARTING)

        val gameSessions = gameSessionRepository!!.findAll()
        Assertions.assertThat<GameSession?>(gameSessions).hasSize(1)
        val gameSession = gameSessions.iterator().next()
        Assertions.assertThat<Game?>(gameSession.getGame()).isEqualTo(game)
        Assertions.assertThat(gameSession.getSession().getSessionId()).isEqualTo(TestUtils.USER_1_SESSION_TOKEN)
        Assertions.assertThat(gameSession.getPlayer().getUsername()).isEqualTo(TestUtils.USER_1_USERNAME)
        Assertions.assertThat(gameSession.getPlayerOptions()).isEqualTo("player1 options")
    }

    @Test
    fun aDifferentPlayerShouldJoinAnExistingGame() {
        // Given
        loginUser(TestUtils.USER_1_SESSION_TOKEN, TestUtils.USER_1_USERNAME)
        val player1JoinRequest = JoinGameRequest.builder()
            .gameType(GameType.UNLIMITED)
            .playerOptions("player1 options")
            .build()

        postForEntity(
            "/game",
            player1JoinRequest,
            JoinGameResponse::class.java,
            TestUtils.USER_1_SESSION_TOKEN
        )

        loginUser(TestUtils.USER_2_SESSION_TOKEN, TestUtils.USER_2_USERNAME)

        val player2JoinRequest = JoinGameRequest.builder()
            .gameType(GameType.UNLIMITED)
            .playerOptions("player2 options")
            .build()

        // When
        val response = postForEntity(
            "/game",
            player2JoinRequest,
            JoinGameResponse::class.java,
            TestUtils.USER_2_SESSION_TOKEN
        )

        // Then
        Assertions.assertThat(response.getResponseBody()!!.getGameId()).isGreaterThan(0)

        val games = gameRepository!!.findAll()
        Assertions.assertThat<Game?>(games).hasSize(1)
        val game = games.iterator().next()
        Assertions.assertThat(game.getId()).isEqualTo(response.getResponseBody()!!.getGameId())
        Assertions.assertThat<GameType?>(game.getType()).isEqualTo(GameType.UNLIMITED)
        Assertions.assertThat<GameStatusType?>(game.getStatus()).isEqualTo(GameStatusType.IN_PROGRESS)

        val gameSessions = gameSessionRepository!!.findAll()
        Assertions.assertThat<GameSession?>(gameSessions).hasSize(2)
        val iterator: MutableIterator<GameSession> = gameSessions.iterator()
        val firstGameSession = iterator.next()
        Assertions.assertThat<Game?>(firstGameSession.getGame()).isEqualTo(game)
        Assertions.assertThat(firstGameSession.getSession().getSessionId()).isEqualTo(TestUtils.USER_1_SESSION_TOKEN)
        Assertions.assertThat(firstGameSession.getPlayer().getUsername()).isEqualTo(TestUtils.USER_1_USERNAME)
        Assertions.assertThat(firstGameSession.getPlayerOptions()).isEqualTo("player1 options")

        val secondGameSession = iterator.next()
        Assertions.assertThat<Game?>(secondGameSession.getGame()).isEqualTo(game)
        Assertions.assertThat(secondGameSession.getSession().getSessionId()).isEqualTo(TestUtils.USER_2_SESSION_TOKEN)
        Assertions.assertThat(secondGameSession.getPlayer().getUsername()).isEqualTo(TestUtils.USER_2_USERNAME)
        Assertions.assertThat(secondGameSession.getPlayerOptions()).isEqualTo("player2 options")
    }

    @Test
    fun samePlayerShouldNotBeAbleToJoinItsOwnGame() {
        // Given
        loginUser(TestUtils.GUEST_SESSION_TOKEN_1, TestUtils.GUEST_USERNAME)
        val player1JoinRequest = JoinGameRequest.builder()
            .gameType(GameType.UNLIMITED)
            .playerOptions("player1 options")
            .build()

        postForEntity(
            "/game",
            player1JoinRequest,
            JoinGameResponse::class.java,
            TestUtils.GUEST_SESSION_TOKEN_1
        )

        loginUser(TestUtils.GUEST_SESSION_TOKEN_2, TestUtils.GUEST_USERNAME)
        val player2JoinRequest = JoinGameRequest.builder()
            .gameType(GameType.UNLIMITED)
            .playerOptions("player2 options")
            .build()

        // When
        val response = postForEntity(
            "/game",
            player2JoinRequest,
            JoinGameResponse::class.java,
            TestUtils.GUEST_SESSION_TOKEN_2
        )

        // Then
        Assertions.assertThat(response.getResponseBody()!!.getGameId()).isGreaterThan(0)
    }

    @Test
    fun userCannotStartAnotherGameIfAlreadyInOne() {
        // Given
        loginUser(TestUtils.USER_1_SESSION_TOKEN, TestUtils.USER_1_USERNAME)
        val player1JoinRequest = JoinGameRequest.builder()
            .gameType(GameType.UNLIMITED)
            .playerOptions("player1 options")
            .build()

        postForEntity(
            "/game",
            player1JoinRequest,
            JoinGameResponse::class.java,
            TestUtils.USER_1_SESSION_TOKEN
        )

        // When
        val response = postForEntity(
            "/game",
            player1JoinRequest,
            JoinGameResponse::class.java,
            TestUtils.USER_1_SESSION_TOKEN
        )

        // Then
        Assertions.assertThat(response.getResponseBody()!!.getError()).isEqualTo("You are already in a game.")
        Assertions.assertThat(response.getResponseBody()!!.getActiveGameId()).isGreaterThan(0)
    }
}