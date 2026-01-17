package com.matag.admin.game.findactive

import com.matag.admin.auth.SecurityContextHolderHelper
import com.matag.admin.game.session.GameSessionRepository
import com.matag.admin.game.session.GameSessionService
import org.springframework.stereotype.Component

@Component
open class FindGameService(
    private val securityContextHolderHelper: SecurityContextHolderHelper,
    private val gameSessionRepository: GameSessionRepository,
    private val gameSessionService: GameSessionService
) {

    open fun findActiveGame(): ActiveGameResponse {
        val session = securityContextHolderHelper.getSession()
        val activeGameSession = gameSessionRepository.findPlayerActiveGameSession(session.sessionId!!)

        if (activeGameSession == null) {
            return ActiveGameResponse()
        }

        val game = activeGameSession.game!!
        val gamePlayers = gameSessionService.getGamePlayers(game)

        return ActiveGameResponse(
            gameId = game.id,
            createdAt = game.createdAt,
            playerName = gamePlayers.playerSession?.player?.username,
            playerOptions = gamePlayers.playerSession?.playerOptions,
            opponentName = if (gamePlayers.opponentSession != null) gamePlayers?.opponentSession
                    ?.player?.username else null,
            opponentOptions = if (gamePlayers.opponentSession != null) gamePlayers?.opponentSession
                    ?.playerOptions else null
        )
    }
}
