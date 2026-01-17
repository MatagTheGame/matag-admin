package com.matag.admin.game.score

import com.matag.admin.game.game.GameResultType
import org.springframework.stereotype.Component
import kotlin.math.pow

@Component
open class EloCalculationService {
    open fun applyEloRating(score1: Score, score2: Score, result: GameResultType) {
        val p1 = probability(score1.elo!!.toFloat(), score2.elo!!.toFloat())
        val p2 = probability(score2.elo!!.toFloat(), score1.elo!!.toFloat())

        when (result) {
            GameResultType.R1 -> {
                score1.elo = Math.round(score1.elo!! + K * (1 - p2))
                score2.elo = Math.round(score2.elo!! + K * (0 - p1))
            }

            GameResultType.R2 -> {
                score1.elo = Math.round(score1.elo!! + K * (0 - p2))
                score2.elo = Math.round(score2.elo!! + K * (1 - p1))
            }

            GameResultType.RX -> {
            }
        }

        println("Updated Ratings:")
        println("user1: " + score1.elo)
        println("user2: " + score2.elo)
    }

    private fun probability(rating1: Float, rating2: Float): Float {
        return 1.0f * 1.0f / (1 + 1.0f * (10.0.pow((1.0f * (rating1 - rating2) / 400).toDouble())).toFloat())
    }

    companion object {
        private const val K = 100
    }
}
