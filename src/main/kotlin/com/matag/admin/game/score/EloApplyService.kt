package com.matag.admin.game.score

import com.matag.admin.game.game.GameResultType
import com.matag.admin.user.MatagUser
import com.matag.admin.user.MatagUserType
import org.springframework.stereotype.Component

@Component
open class EloApplyService(
    private val scoreRepository: ScoreRepository,
    private val eloCalculationService: EloCalculationService
) {
    open fun apply(user1: MatagUser, user2: MatagUser, result: GameResultType) {
        if (user1.type == MatagUserType.USER && user2.type == MatagUserType.USER) {
            val score1 = scoreRepository.findByMatagUser(user1)
            val score2 = scoreRepository.findByMatagUser(user2)
            eloCalculationService.applyEloRating(score1!!, score2!!, result)
        }
    }
}
