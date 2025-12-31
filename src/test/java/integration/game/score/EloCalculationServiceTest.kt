package integration.game.score

import com.matag.admin.game.game.GameResultType
import com.matag.admin.game.score.EloCalculationService
import com.matag.admin.game.score.Score
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class EloCalculationServiceTest {
    private val eloCalculationService = EloCalculationService()

    @Test
    fun sameEloPlayer1Winner() {
        // Given
        val user1 = Score.builder().elo(1000).build()
        val user2 = Score.builder().elo(1000).build()

        // When
        eloCalculationService.applyEloRating(user1, user2, GameResultType.R1)

        // Then
        Assertions.assertThat(user1.elo).isEqualTo(1050)
        Assertions.assertThat(user2.elo).isEqualTo(950)
    }

    @Test
    fun sameEloPlayer2Winner() {
        // Given
        val user1 = Score.builder().elo(1000).build()
        val user2 = Score.builder().elo(1000).build()

        // When
        eloCalculationService.applyEloRating(user1, user2, GameResultType.R2)

        // Then
        Assertions.assertThat(user1.elo).isEqualTo(950)
        Assertions.assertThat(user2.elo).isEqualTo(1050)
    }

    @Test
    fun highEloWins() {
        // Given
        val user1 = Score.builder().elo(1800).build()
        val user2 = Score.builder().elo(1000).build()

        // When
        eloCalculationService.applyEloRating(user1, user2, GameResultType.R1)

        // Then
        Assertions.assertThat(user1.elo).isEqualTo(1801)
        Assertions.assertThat(user2.elo).isEqualTo(999)
    }

    @Test
    fun highEloLoses() {
        // Given
        val user1 = Score.builder().elo(1800).build()
        val user2 = Score.builder().elo(1000).build()

        // When
        eloCalculationService.applyEloRating(user1, user2, GameResultType.R2)

        // Then
        Assertions.assertThat(user1.elo).isEqualTo(1701)
        Assertions.assertThat(user2.elo).isEqualTo(1099)
    }
}
