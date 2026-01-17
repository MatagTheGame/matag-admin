package com.matag.admin.game.finish

import com.matag.admin.game.game.Game
import com.matag.admin.game.game.GameRepository
import com.matag.admin.game.game.GameStatusType
import com.matag.admin.game.result.ResultService
import com.matag.admin.game.score.EloApplyService
import com.matag.admin.game.session.GamePlayers
import com.matag.admin.game.session.GameSession
import com.matag.admin.game.session.GameSessionRepository
import com.matag.admin.game.session.GameSessionService
import com.matag.admin.game.finish.FinishGameRequest
import lombok.AllArgsConstructor
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.LocalDateTime
import java.util.function.Consumer

@Component
open class FinishGameService(
    private val clock: Clock,
    private val gameRepository: GameRepository,
    private val gameSessionRepository: GameSessionRepository,
    private val gameSessionService: GameSessionService,
    private val resultService: ResultService,
    private val eloApplyService: EloApplyService,
) {
    @Transactional
    open fun finish(gameId: Long, request: FinishGameRequest) {
        val gameOptional = gameRepository.findById(gameId)
        gameOptional.ifPresent(Consumer { game: Game? ->
            if (game?.status == GameStatusType.IN_PROGRESS) {
                val gamePlayers = gameSessionService.getGamePlayers(game)
                finishGame(game, gamePlayers, request.winnerSessionId)
            }
        })
    }

    open fun finishGame(game: Game, gamePlayers: GamePlayers, winnerSessionId: String?) {
        val session1 = gamePlayers.getPlayerSession()
        val session2 = gamePlayers.getOpponentSession()

        val gameResultType = resultService.getResult(gamePlayers, winnerSessionId)
        game.status = GameStatusType.FINISHED
        game.result = gameResultType
        game.finishedAt = LocalDateTime.now(clock)
        gameRepository.save(game)

        gamePlayers.playerSession.session = null
        gameSessionRepository.save(session1)
        gamePlayers.opponentSession.session = null
        gameSessionRepository.save(session2)

        eloApplyService.apply(session1.player, session2.player, gameResultType)
    }
}
