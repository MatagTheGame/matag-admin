package application.game.score;

import application.AbstractApplicationTest;
import com.matag.admin.game.score.ScoreRepository;
import com.matag.admin.game.score.ScoreResponse;
import com.matag.admin.game.score.ScoresResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static application.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

public class ScoresControllerTest extends AbstractApplicationTest {
    @Autowired
    private ScoreRepository scoreRepository;

    @Test
    public void returnsForbiddenForNonLoggedInUsers() {
        // When
        var response = getForEntity("/game/scores", ScoresResponse.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(FORBIDDEN);
    }

    @Test
    public void retrieveScores() {
        // Given
        loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME);

        scoreRepository.save(scoreRepository.findByUsername(USER_1_USERNAME).toBuilder().elo(1600).wins(1).draws(0).losses(0).build());
        scoreRepository.save(scoreRepository.findByUsername(USER_2_USERNAME).toBuilder().elo(1400).wins(0).draws(0).losses(1).build());

        // When
        var response = getForEntity("/game/scores", ScoresResponse.class, USER_1_SESSION_TOKEN);

        // Then
        assertThat(response.getStatus()).isEqualTo(OK);
        assertThat(response.getResponseBody().getScores()).contains(
                ScoreResponse.builder().rank(1).player(USER_1_USERNAME).elo(1600).wins(1).draws(0).losses(0).build(),
                ScoreResponse.builder().rank(2).player(USER_2_USERNAME).elo(1400).wins(0).draws(0).losses(1).build()
        );
    }
}