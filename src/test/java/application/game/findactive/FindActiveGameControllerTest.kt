package application.game.findactive;

import application.AbstractApplicationTest;
import com.matag.admin.game.findactive.ActiveGameResponse;
import com.matag.admin.game.join.JoinGameRequest;
import com.matag.admin.game.join.JoinGameResponse;
import org.junit.jupiter.api.Test;

import static application.TestUtils.USER_1_SESSION_TOKEN;
import static application.TestUtils.USER_1_USERNAME;
import static com.matag.admin.game.game.GameType.UNLIMITED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

public class FindActiveGameControllerTest extends AbstractApplicationTest {
  @Test
  public void shouldFindNoGames() {
    // Given
    loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME);

    // When
    var response = getForEntity("/game", ActiveGameResponse.class);

    // Then
    assertThat(response.getResponseBody().getGameId()).isNull();
  }

  @Test
  public void shouldFindAnActiveGame() {
    // Given
    loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME);

    var request = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("player1 options")
      .build();
    postForEntity("/game", request, JoinGameResponse.class, USER_1_SESSION_TOKEN);

    // When
    var response = getForEntity("/game", ActiveGameResponse.class, USER_1_SESSION_TOKEN);

    // Then
    assertThat(response.getStatus()).isEqualTo(OK);
    assertThat(response.getResponseBody().getGameId()).isGreaterThan(0);
    assertThat(response.getResponseBody().getCreatedAt()).isNotNull();
    assertThat(response.getResponseBody().getPlayerName()).isEqualTo("User1");
    assertThat(response.getResponseBody().getPlayerOptions()).isEqualTo("player1 options");
    assertThat(response.getResponseBody().getOpponentName()).isNull();
    assertThat(response.getResponseBody().getOpponentOptions()).isNull();
  }
}