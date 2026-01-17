package com.matag.admin.game.score

import lombok.AllArgsConstructor
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/game")
@AllArgsConstructor
open class ScoresController (
    private val scoreRepository: ScoreRepository
) {

    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    @GetMapping("/scores")
    open fun gameScore(): ScoresResponse? {
        val scores = scoreRepository!!.findAll()
        return ScoresResponse(
            scores = toScoreResponse(scores)
        )
    }

    private fun toScoreResponse(scores: List<Score>): List<ScoreResponse> {
        val scoreResponses = ArrayList<ScoreResponse>(scores.size)
        for (i in scores.indices) {
            val score = scores.get(i)
            scoreResponses.add(
                ScoreResponse(
                    rank = i + 1,
                    player = score.matagUser?.username,
                    elo = score.elo,
                    wins = score.wins,
                    draws = score.draws,
                    losses = score.losses
                )
            )
        }
        return scoreResponses
    }
}
