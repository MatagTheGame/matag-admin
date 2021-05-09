package application.game.history;

import static application.TestUtils.USER_1_SESSION_TOKEN;
import static application.TestUtils.USER_1_USERNAME;
import static application.TestUtils.USER_2_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.time.LocalDateTime;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.matag.admin.game.game.Game;
import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.game.GameResultType;
import com.matag.admin.game.game.GameStatusType;
import com.matag.admin.game.game.GameType;
import com.matag.admin.game.game.GameUserResultType;
import com.matag.admin.game.history.GameHistory;
import com.matag.admin.game.history.GamesHistoryResponse;
import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;
import com.matag.admin.user.MatagUser;

import application.AbstractApplicationTest;

public class GamesHistoryControllerTest extends AbstractApplicationTest {
  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private GameSessionRepository gameSessionRepository;

  @Test
  public void returnsForbiddenForNonLoggedInUsers() {
    // When
    var response = restTemplate.getForEntity("/game/history", GamesHistoryResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
  }

  @Test
  public void retrieveGameHistory() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    var user1 = loadUser(USER_1_USERNAME);
    var user2 = loadUser(USER_2_USERNAME);

    var game1 = createGame(GameResultType.R1);
    createGameSession(game1, user1);
    createGameSession(game1, user2);

    var game2 = createGame(GameResultType.R2);
    createGameSession(game2, user1);
    createGameSession(game2, user2);

    var game3 = createGame(GameResultType.RX);
    createGameSession(game3, user2);
    createGameSession(game3, user1);

    // When
    var gamesHistoryResponse = restTemplate.getForObject("/game/history", GamesHistoryResponse.class);

    // Then
    assertThat(gamesHistoryResponse.getGamesHistory()).hasSize(3);
    assertThat(gamesHistoryResponse.getGamesHistory().get(0)).isEqualToIgnoringGivenFields(
      GameHistory.builder()
        .startedTime(LocalDateTime.now(clock))
        .finishedTime(LocalDateTime.now(clock))
        .type(GameType.UNLIMITED)
        .result(GameUserResultType.WIN)
        .player1Name(USER_1_USERNAME)
        .player1Options("User1 options")
        .player2Name(USER_2_USERNAME)
        .player2Options("User2 options")
        .build(),
      "gameId"
    );
    assertThat(gamesHistoryResponse.getGamesHistory().get(1)).isEqualToIgnoringGivenFields(
      GameHistory.builder()
        .startedTime(LocalDateTime.now(clock))
        .finishedTime(LocalDateTime.now(clock))
        .type(GameType.UNLIMITED)
        .result(GameUserResultType.LOST)
        .player1Name(USER_1_USERNAME)
        .player1Options("User1 options")
        .player2Name(USER_2_USERNAME)
        .player2Options("User2 options")
        .build(),
      "gameId"
    );
    assertThat(gamesHistoryResponse.getGamesHistory().get(2)).isEqualToIgnoringGivenFields(
      GameHistory.builder()
        .startedTime(LocalDateTime.now(clock))
        .finishedTime(LocalDateTime.now(clock))
        .type(GameType.UNLIMITED)
        .result(GameUserResultType.DRAW)
        .player1Name(USER_2_USERNAME)
        .player1Options("User2 options")
        .player2Name(USER_1_USERNAME)
        .player2Options("User1 options")
        .build(),
      "gameId"
    );
  }

  private Game createGame(GameResultType result) {
    var game = Game.builder()
      .createdAt(LocalDateTime.now(clock))
      .type(GameType.UNLIMITED)
      .status(GameStatusType.FINISHED)
      .result(result)
      .finishedAt(LocalDateTime.now(clock))
      .build();
    gameRepository.save(game);
    return game;
  }

  private void createGameSession(Game game, MatagUser user) {
    var gameSession = GameSession.builder()
      .game(game)
      .player(user)
      .playerOptions(user.getUsername() + " options")
      .build();
    gameSessionRepository.save(gameSession);
  }
}