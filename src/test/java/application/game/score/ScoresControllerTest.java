package application.game.score;

import static application.TestUtils.USER_1_SESSION_TOKEN;
import static application.TestUtils.USER_1_USERNAME;
import static application.TestUtils.USER_2_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.matag.admin.game.score.Score;
import com.matag.admin.game.score.ScoreRepository;
import com.matag.admin.game.score.ScoreResponse;
import com.matag.admin.game.score.ScoresResponse;

import application.AbstractApplicationTest;

public class ScoresControllerTest extends AbstractApplicationTest {
  @Autowired
  private ScoreRepository scoreRepository;

  @Test
  public void returnsForbiddenForNonLoggedInUsers() {
    // When
    var response = restTemplate.getForEntity("/game/scores", ScoresResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
  }

  @Test
  public void retrieveScores() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    var user1 = loadUser(USER_1_USERNAME);
    var user2 = loadUser(USER_2_USERNAME);

    scoreRepository.save(Score.builder().matagUser(user1).elo(1600).wins(1).draws(0).losses(0).build());
    scoreRepository.save(Score.builder().matagUser(user2).elo(1400).wins(0).draws(0).losses(1).build());

    // When
    var response = restTemplate.getForObject("/game/scores", ScoresResponse.class);

    // Then
    assertThat(response.getScores()).contains(
      ScoreResponse.builder().rank(1).player(user1.getUsername()).elo(1600).wins(1).draws(0).losses(0).build(),
      ScoreResponse.builder().rank(2).player(user2.getUsername()).elo(1400).wins(0).draws(0).losses(1).build()
    );
  }
}