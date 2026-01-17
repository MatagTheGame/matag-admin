package application.game.score

import application.AbstractApplicationTest
import application.TestUtils
import com.matag.admin.game.score.Score
import com.matag.admin.game.score.ScoreRepository
import com.matag.admin.game.score.ScoreResponse
import com.matag.admin.game.score.ScoresResponse
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

class ScoresControllerTest : AbstractApplicationTest() {
    @Test
    fun returnsForbiddenForNonLoggedInUsers() {
        // When
        val response = getForEntity("/game/scores", ScoresResponse::class.java)

        // Then
        assertThat(response.status).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun retrieveScores() {
        // Given
        loginUser(TestUtils.USER_1_SESSION_TOKEN, TestUtils.USER_1_USERNAME)

        scoreRepository.save(scoreRepository.findByUsername(TestUtils.USER_1_USERNAME)?.copy(elo = 1600, wins = 1, draws = 0, losses = 0)!!)
        scoreRepository.save(scoreRepository.findByUsername(TestUtils.USER_2_USERNAME)?.copy(elo = 1400, wins = 0, draws = 0, losses = 1)!!)

        // When
        val response =
            getForEntity("/game/scores", ScoresResponse::class.java, TestUtils.USER_1_SESSION_TOKEN)

        // Then
        assertThat(response.status).isEqualTo(HttpStatus.OK)
        assertThat(response.getResponseBody()?.scores).contains(
            ScoreResponse(rank = 1, player = TestUtils.USER_1_USERNAME, elo = 1600, wins = 1, draws = 0, losses = 0),
            ScoreResponse(rank = 2, player = TestUtils.USER_2_USERNAME, elo = 1400, wins = 0, draws = 0, losses = 1)
        )
    }
}