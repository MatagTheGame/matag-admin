package application.game.join;

import application.AbstractApplicationTest;
import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.join.JoinGameRequest;
import com.matag.admin.game.join.JoinGameResponse;
import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static application.TestUtils.*;
import static com.matag.admin.game.game.GameStatusType.IN_PROGRESS;
import static com.matag.admin.game.game.GameStatusType.STARTING;
import static com.matag.admin.game.game.GameType.UNLIMITED;
import static org.assertj.core.api.Assertions.assertThat;

public class JoinGameControllerTest extends AbstractApplicationTest {
  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private GameSessionRepository gameSessionRepository;

  @Test
  public void shouldCreateAGame() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    var request = new JoinGameRequest(UNLIMITED, "player1 options");

    // When
    var response = restTemplate.postForObject("/game", request, JoinGameResponse.class);

    // Then
    assertThat(response.gameId()).isGreaterThan(0);

    var games = gameRepository.findAll();
    assertThat(games).hasSize(1);
    var game = games.iterator().next();
    assertThat(game.getId()).isEqualTo(response.gameId());
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
    userIsLoggedIn(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    var player1JoinRequest = new JoinGameRequest(UNLIMITED, "player1 options");

    restTemplate.postForObject("/game", player1JoinRequest, JoinGameResponse.class);

    userIsLoggedIn(USER_2_SESSION_TOKEN, USER_2_USERNAME);

    var player2JoinRequest = new JoinGameRequest(UNLIMITED, "player2 options");

    // When
    var response = restTemplate.postForObject("/game", player2JoinRequest, JoinGameResponse.class);

    // Then
    assertThat(response.gameId()).isGreaterThan(0);

    var games = gameRepository.findAll();
    assertThat(games).hasSize(1);
    var game = games.iterator().next();
    assertThat(game.getId()).isEqualTo(response.gameId());
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
    userIsLoggedIn(GUEST_SESSION_TOKEN_1, GUEST_USERNAME);
    var player1JoinRequest = new JoinGameRequest(UNLIMITED, "player1 options");

    restTemplate.postForObject("/game", player1JoinRequest, JoinGameResponse.class);

    userIsLoggedIn(GUEST_SESSION_TOKEN_2, GUEST_USERNAME);
    var player2JoinRequest = new JoinGameRequest(UNLIMITED, "player2 options");

    // When
    var response = restTemplate.postForObject("/game", player2JoinRequest, JoinGameResponse.class);

    // Then
    assertThat(response.gameId()).isGreaterThan(0);
  }

  @Test
  public void userCannotStartAnotherGameIfAlreadyInOne() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    var player1JoinRequest = new JoinGameRequest(UNLIMITED, "player1 options");

    restTemplate.postForObject("/game", player1JoinRequest, JoinGameResponse.class);

    // When
    var response = restTemplate.postForObject("/game", player1JoinRequest, JoinGameResponse.class);

    // Then
    assertThat(response.error()).isEqualTo("You are already in a game.");
    assertThat(response.activeGameId()).isGreaterThan(0);
  }
}