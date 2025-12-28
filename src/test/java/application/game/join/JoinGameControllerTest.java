package application.game.join;

import static application.TestUtils.GUEST_SESSION_TOKEN_1;
import static application.TestUtils.GUEST_SESSION_TOKEN_2;
import static application.TestUtils.GUEST_USERNAME;
import static application.TestUtils.USER_1_SESSION_TOKEN;
import static application.TestUtils.USER_1_USERNAME;
import static application.TestUtils.USER_2_SESSION_TOKEN;
import static application.TestUtils.USER_2_USERNAME;
import static com.matag.admin.game.game.GameStatusType.IN_PROGRESS;
import static com.matag.admin.game.game.GameStatusType.STARTING;
import static com.matag.admin.game.game.GameType.UNLIMITED;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.join.JoinGameRequest;
import com.matag.admin.game.join.JoinGameResponse;
import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;

import application.AbstractApplicationTest;

public class JoinGameControllerTest extends AbstractApplicationTest {
  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private GameSessionRepository gameSessionRepository;

  @Test
  public void shouldCreateAGame() {
    // Given
    loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    var request = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("player1 options")
      .build();

    // When
    var response = postForEntity("/game", request, JoinGameResponse.class, USER_1_SESSION_TOKEN);

    // Then
    assertThat(response.getResponseBody().getGameId()).isGreaterThan(0);

    var games = gameRepository.findAll();
    assertThat(games).hasSize(1);
    var game = games.iterator().next();
    assertThat(game.getId()).isEqualTo(response.getResponseBody().getGameId());
    assertThat(game.getType()).isEqualTo(UNLIMITED);
    assertThat(game.getStatus()).isEqualTo(STARTING);

    var gameSessions = gameSessionRepository.findAll();
    assertThat(gameSessions).hasSize(1);
    GameSession gameSession = gameSessions.iterator().next();
    assertThat(gameSession.getGame()).isEqualTo(game);
    assertThat(gameSession.getSession().getSessionId()).isEqualTo(USER_1_SESSION_TOKEN);
    assertThat(gameSession.getPlayer().getUsername()).isEqualTo(USER_1_USERNAME);
    assertThat(gameSession.getPlayerOptions()).isEqualTo("player1 options");
  }

  @Test
  public void aDifferentPlayerShouldJoinAnExistingGame() {
    // Given
    loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    var player1JoinRequest = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("player1 options")
      .build();

    postForEntity("/game", player1JoinRequest, JoinGameResponse.class, USER_1_SESSION_TOKEN);

    loginUser(USER_2_SESSION_TOKEN, USER_2_USERNAME);

    var player2JoinRequest = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("player2 options")
      .build();

    // When
    var response = postForEntity("/game", player2JoinRequest, JoinGameResponse.class, USER_2_SESSION_TOKEN);

    // Then
    assertThat(response.getResponseBody().getGameId()).isGreaterThan(0);

    var games = gameRepository.findAll();
    assertThat(games).hasSize(1);
    var game = games.iterator().next();
    assertThat(game.getId()).isEqualTo(response.getResponseBody().getGameId());
    assertThat(game.getType()).isEqualTo(UNLIMITED);
    assertThat(game.getStatus()).isEqualTo(IN_PROGRESS);

    var gameSessions = gameSessionRepository.findAll();
    assertThat(gameSessions).hasSize(2);
    var iterator = gameSessions.iterator();
    var firstGameSession = iterator.next();
    assertThat(firstGameSession.getGame()).isEqualTo(game);
    assertThat(firstGameSession.getSession().getSessionId()).isEqualTo(USER_1_SESSION_TOKEN);
    assertThat(firstGameSession.getPlayer().getUsername()).isEqualTo(USER_1_USERNAME);
    assertThat(firstGameSession.getPlayerOptions()).isEqualTo("player1 options");

    var secondGameSession = iterator.next();
    assertThat(secondGameSession.getGame()).isEqualTo(game);
    assertThat(secondGameSession.getSession().getSessionId()).isEqualTo(USER_2_SESSION_TOKEN);
    assertThat(secondGameSession.getPlayer().getUsername()).isEqualTo(USER_2_USERNAME);
    assertThat(secondGameSession.getPlayerOptions()).isEqualTo("player2 options");
  }

  @Test
  public void samePlayerShouldNotBeAbleToJoinItsOwnGame() {
    // Given
    loginUser(GUEST_SESSION_TOKEN_1, GUEST_USERNAME);
    var player1JoinRequest = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("player1 options")
      .build();

    postForEntity("/game", player1JoinRequest, JoinGameResponse.class, GUEST_SESSION_TOKEN_1);

    loginUser(GUEST_SESSION_TOKEN_2, GUEST_USERNAME);
    var player2JoinRequest = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("player2 options")
      .build();

    // When
    var response = postForEntity("/game", player2JoinRequest, JoinGameResponse.class, GUEST_SESSION_TOKEN_2);

    // Then
    assertThat(response.getResponseBody().getGameId()).isGreaterThan(0);
  }

  @Test
  public void userCannotStartAnotherGameIfAlreadyInOne() {
    // Given
    loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    var player1JoinRequest = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("player1 options")
      .build();

    postForEntity("/game", player1JoinRequest, JoinGameResponse.class, USER_1_SESSION_TOKEN);

    // When
    var response = postForEntity("/game", player1JoinRequest, JoinGameResponse.class, USER_1_SESSION_TOKEN);

    // Then
    assertThat(response.getResponseBody().getError()).isEqualTo("You are already in a game.");
    assertThat(response.getResponseBody().getActiveGameId()).isGreaterThan(0);
  }
}