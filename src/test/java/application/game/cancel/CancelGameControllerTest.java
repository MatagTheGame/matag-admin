package application.game.cancel;

import application.AbstractApplicationTest;
import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.join.JoinGameRequest;
import com.matag.admin.game.join.JoinGameResponse;
import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    var request = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("player1 options")
      .build();
    var joinGameResponse = postForEntity("/game", request, JoinGameResponse.class, USER_1_SESSION_TOKEN);
    var gameId = joinGameResponse.getResponseBody().getGameId();

    // When
    var response = delete("/game/" + gameId, USER_1_SESSION_TOKEN);

    // Then
    assertThat(response.getStatus()).isEqualTo(OK);
    assertThat(gameSessionRepository.findAll()).hasSize(0);
    assertThat(gameRepository.findAll()).hasSize(0);
  }

  @Test
  public void shouldCancelAGameWhereSomebodyJoined() {
    // Given
    loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    var request1 = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("player1 options")
      .build();
    var joinGameResponse = postForEntity("/game", request1, JoinGameResponse.class, USER_1_SESSION_TOKEN);
    var gameId = joinGameResponse.getResponseBody().getGameId();

    loginUser(USER_2_SESSION_TOKEN, USER_2_USERNAME);
    var request2 = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("player2 options")
      .build();
    postForEntity("/game", request2, JoinGameResponse.class, USER_2_SESSION_TOKEN);

    // When
    loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    var response = delete("/game/" + gameId, USER_1_SESSION_TOKEN);

    // Then
    assertThat(response.getStatus()).isEqualTo(OK);
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