package com.matag.admin.game.score

import com.matag.admin.game.game.GameType
import com.matag.admin.user.MatagUser
import lombok.AllArgsConstructor
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
@AllArgsConstructor
class ScoreService(
    private val scoreRepository: ScoreRepository,
    private val clock: Clock
) {

    fun createStartingScore(matagUser: MatagUser?) {
        scoreRepository.save(
            Score(
                matagUser = matagUser,
                elo = STARTING_ELO,
                type = GameType.UNLIMITED,
                wins = 0,
                draws = 0,
                losses = 0,
                lastGamePlayedAt = LocalDateTime.now(clock)
            )
        )
    }

    companion object {
        private const val STARTING_ELO = 1000
    }
}
