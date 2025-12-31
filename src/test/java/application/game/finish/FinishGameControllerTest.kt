package application.game.finish;

import static application.TestUtils.USER_1_SESSION_TOKEN;
import static application.TestUtils.USER_1_USERNAME;
import static application.TestUtils.USER_2_SESSION_TOKEN;
import static application.TestUtils.USER_2_USERNAME;
import static com.matag.admin.game.game.GameResultType.R1;
import static com.matag.admin.game.game.GameStatusType.FINISHED;
import static com.matag.admin.game.game.GameType.UNLIMITED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.join.JoinGameRequest;
import com.matag.admin.game.join.JoinGameResponse;
import com.matag.admin.game.score.ScoreRepository;
import com.matag.admin.game.session.GameSessionRepository;
import com.matag.adminentities.FinishGameRequest;

import application.AbstractApplicationTest;

public class FinishGameControllerTest extends AbstractApplicationTest {
  @Autowired private GameRepository gameRepository;
  @Autowired private GameSessionRepository gameSessionRepository;
  @Autowired private ScoreRepository scoreRepository;

  @Test
  public void requiresAdminAuthentication() {
    // Given
    loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME);

    // When
    var finishGameRequest = new FinishGameRequest(USER_1_SESSION_TOKEN);
    var response = postForEntity("/game/1/finish", finishGameRequest, Object.class, USER_1_SESSION_TOKEN);

    // Then
    assertThat(response.getStatus()).isEqualTo(FORBIDDEN);
  }

  @Test
  public void shouldFinishAGame() {
    // Given
    loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    var request1 = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("player1 options")
      .build();
    var joinGameResponse = postForEntity("/game", request1, JoinGameResponse.class, USER_1_SESSION_TOKEN);
    Long gameId = joinGameResponse.getResponseBody().getGameId();

    loginUser(USER_2_SESSION_TOKEN, USER_2_USERNAME);
    var request2 = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("player2 options")
      .build();
    postForEntity("/game", request2, JoinGameResponse.class, USER_2_SESSION_TOKEN);

    // When
    var finishGameRequest = new FinishGameRequest(USER_1_SESSION_TOKEN);
    var response = postForAdmin("/game/" + gameId + "/finish", finishGameRequest);

    // Then
    assertThat(response.getStatus()).isEqualTo(OK);
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