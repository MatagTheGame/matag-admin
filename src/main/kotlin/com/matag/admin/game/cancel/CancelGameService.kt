package com.matag.admin.game.cancel

import com.matag.admin.auth.SecurityContextHolderHelper
import com.matag.admin.game.finish.FinishGameService
import com.matag.admin.game.game.GameRepository
import com.matag.admin.game.session.GamePlayers
import com.matag.admin.game.session.GameSessionRepository
import com.matag.admin.game.session.GameSessionService
import com.matag.admin.session.MatagSession
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
open class CancelGameService(
    private val securityContextHolderHelper: SecurityContextHolderHelper,
    private val gameRepository: GameRepository,
    private val gameSessionRepository: GameSessionRepository,
    private val gameSessionService: GameSessionService,
    private val finishGameService: FinishGameService
) {

    @Transactional
    open fun cancel(gameId: Long) {
        val session = securityContextHolderHelper.getSession()
        val activeGameSession = gameSessionRepository.findPlayerActiveGameSession(session.sessionId!!)
        if (activeGameSession != null) {
            if (activeGameSession.game?.id == gameId) {
                val game = activeGameSession.game
                val gamePlayers = gameSessionService.getGamePlayers(game!!)
                if (gamePlayers.opponentSession == null) {
                    gameSessionRepository.delete(gamePlayers.playerSession!!)
                    gameRepository.delete(game)
                } else {
                    val winnerSessionId = findOpponentSessionId(gamePlayers, session)
                    finishGameService.finishGame(game, gamePlayers, winnerSessionId)
                }
            }
        }
    }

    private fun findOpponentSessionId(gamePlayers: GamePlayers, session: MatagSession): String? {
        return if (gamePlayers.playerSession?.session?.id == session.id) {
            gamePlayers.opponentSession?.session?.sessionId
        } else {
            gamePlayers.playerSession?.session?.sessionId
        }
    }
}
