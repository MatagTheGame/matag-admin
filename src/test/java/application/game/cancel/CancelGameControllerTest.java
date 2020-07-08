package application.game.cancel;

import application.AbstractApplicationTest;
import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.join.JoinGameRequest;
import com.matag.admin.game.join.JoinGameResponse;
import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static application.TestUtils.*;
import static com.matag.admin.game.game.GameResultType.R2;
import static com.matag.admin.game.game.GameStatusType.FINISHED;
import static com.matag.admin.game.game.GameType.UNLIMITED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

public class CancelGameControllerTest extends AbstractApplicationTest {
  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private GameSessionRepository gameSessionRepository;

  @Test
  public void shouldCancelAGameWhereNobodyJoined() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    var request = new JoinGameRequest(UNLIMITED, "player1 options");
    var joinGameResponse = restTemplate.postForObject("/game", request, JoinGameResponse.class);
    var gameId = joinGameResponse.gameId();

    // When
    var response = restTemplate.exchange("/game/" + gameId, HttpMethod.DELETE, null, String.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(OK);
    assertThat(gameSessionRepository.findAll()).hasSize(0);
    assertThat(gameRepository.findAll()).hasSize(0);
  }

  @Test
  public void shouldCancelAGameWhereSomebodyJoined() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    var request1 = new JoinGameRequest(UNLIMITED, "player1 options");
    var joinGameResponse = restTemplate.postForObject("/game", request1, JoinGameResponse.class);
    var gameId = joinGameResponse.gameId();

    userIsLoggedIn(USER_2_SESSION_TOKEN, USER_2_USERNAME);
    var request2 = new JoinGameRequest(UNLIMITED, "player2 options");
    restTemplate.postForObject("/game", request2, JoinGameResponse.class);

    // When
    userIsLoggedIn(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    var response = restTemplate.exchange("/game/" + gameId, HttpMethod.DELETE, null, String.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(OK);
    var game = gameRepository.findById(gameId);
    assertThat(game).isPresent();
    assertThat(game.get().getStatus()).isEqualTo(FINISHED);
    assertThat(game.get().getResult()).isEqualTo(R2);
    assertThat(game.get().getFinishedAt()).isNotNull();

    List<GameSession> gameSessions = StreamSupport.stream(gameSessionRepository.findAll().spliterator(), false).collect(Collectors.toList());
    assertThat(gameSessions).hasSize(2);
    assertThat(gameSessions.get(0).getSession()).isNull();
    assertThat(gameSessions.get(1).getSession()).isNull();
  }
}