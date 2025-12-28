package integration.game.score;

import static com.matag.admin.game.game.GameResultType.R1;
import static com.matag.admin.game.game.GameResultType.R2;
import static org.assertj.core.api.Assertions.assertThat;

import com.matag.admin.game.score.EloCalculationService;
import com.matag.admin.game.score.Score;
import org.junit.jupiter.api.Test;

public class EloCalculationServiceTest {
  private final EloCalculationService eloCalculationService = new EloCalculationService();

  @Test
  public void sameEloPlayer1Winner() {
    // Given
    Score user1 = Score.builder().elo(1000).build();
    Score user2 = Score.builder().elo(1000).build();

    // When
    eloCalculationService.applyEloRating(user1, user2, R1);

    // Then
    assertThat(user1.getElo()).isEqualTo(1050);
    assertThat(user2.getElo()).isEqualTo(950);
  }

  @Test
  public void sameEloPlayer2Winner() {
    // Given
    Score user1 = Score.builder().elo(1000).build();
    Score user2 = Score.builder().elo(1000).build();

    // When
    eloCalculationService.applyEloRating(user1, user2, R2);

    // Then
    assertThat(user1.getElo()).isEqualTo(950);
    assertThat(user2.getElo()).isEqualTo(1050);
  }

  @Test
  public void highEloWins() {
    // Given
    Score user1 = Score.builder().elo(1800).build();
    Score user2 = Score.builder().elo(1000).build();

    // When
    eloCalculationService.applyEloRating(user1, user2, R1);

    // Then
    assertThat(user1.getElo()).isEqualTo(1801);
    assertThat(user2.getElo()).isEqualTo(999);
  }

  @Test
  public void highEloLoses() {
    // Given
    Score user1 = Score.builder().elo(1800).build();
    Score user2 = Score.builder().elo(1000).build();

    // When
    eloCalculationService.applyEloRating(user1, user2, R2);

    // Then
    assertThat(user1.getElo()).isEqualTo(1701);
    assertThat(user2.getElo()).isEqualTo(1099);
  }
}
