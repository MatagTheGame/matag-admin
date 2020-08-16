package application.game.finish;

import application.AbstractApplicationTest;
import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.join.JoinGameRequest;
import com.matag.admin.game.join.JoinGameResponse;
import com.matag.admin.game.score.ScoreRepository;
import com.matag.admin.game.session.GameSessionRepository;
import com.matag.adminentities.FinishGameRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static application.TestUtils.*;
import static com.matag.admin.game.game.GameResultType.R1;
import static com.matag.admin.game.game.GameStatusType.FINISHED;
import static com.matag.admin.game.game.GameType.UNLIMITED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

public class FinishGameControllerTest extends AbstractApplicationTest {
  @Autowired private GameRepository gameRepository;
  @Autowired private GameSessionRepository gameSessionRepository;
  @Autowired private ScoreRepository scoreRepository;

  @Test
  public void requiresAdminAuthentication() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, USER_1_USERNAME);

    // When
    var finishGameRequest = new FinishGameRequest(USER_1_SESSION_TOKEN);
    var response = restTemplate.postForEntity("/game/1/finish", finishGameRequest, Object.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
  }

  @Test
  public void shouldFinishAGame() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    var request1 = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("player1 options")
      .build();
    var joinGameResponse = restTemplate.postForObject("/game", request1, JoinGameResponse.class);
    Long gameId = joinGameResponse.getGameId();

    userIsLoggedIn(USER_2_SESSION_TOKEN, USER_2_USERNAME);
    var request2 = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("player2 options")
      .build();
    restTemplate.postForObject("/game", request2, JoinGameResponse.class);

    // When
    adminAuthentication();
    var finishGameRequest = new FinishGameRequest(USER_1_SESSION_TOKEN);
    var response = restTemplate.postForEntity("/game/" + gameId + "/finish", finishGameRequest, Object.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(OK);
    var game = gameRepository.findById(gameId);
    assertThat(game).isPresent();
    assertThat(game.get().getStatus()).isEqualTo(FINISHED);
    assertThat(game.get().getResult()).isEqualTo(R1);
    assertThat(game.get().getFinishedAt()).isNotNull();

    var gameSessions = StreamSupport.stream(gameSessionRepository.findAll().spliterator(), false).collect(Collectors.toList());
    assertThat(gameSessions).hasSize(2);
    assertThat(gameSessions.get(0).getSession()).isNull();
    assertThat(gameSessions.get(1).getSession()).isNull();

    // Check elo score
    assertThat(scoreRepository.findByUsername(USER_1_USERNAME).getElo()).isEqualTo(1050);
    assertThat(scoreRepository.findByUsername(USER_2_USERNAME).getElo()).isEqualTo(950);
  }
}