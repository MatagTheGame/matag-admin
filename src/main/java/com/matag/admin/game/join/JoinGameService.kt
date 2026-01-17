package com.matag.admin.game.join

import com.matag.admin.auth.SecurityContextHolderHelper
import com.matag.admin.game.game.Game
import com.matag.admin.game.game.GameRepository
import com.matag.admin.game.game.GameStatus
import com.matag.admin.game.session.GameSession
import com.matag.admin.game.session.GameSessionRepository
import com.matag.admin.game.session.GameSessionService
import lombok.AllArgsConstructor
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.LocalDateTime

@Component
@AllArgsConstructor
open class JoinGameService(
    private val securityContextHolderHelper: SecurityContextHolderHelper,
    private val gameRepository: GameRepository,
    private val gameSessionRepository: GameSessionRepository,
    private val gameSessionService: GameSessionService,
    private val clock: Clock
) {
    
    @Transactional
    open fun joinGame(joinGameRequest: JoinGameRequest): JoinGameResponse {
        val user = securityContextHolderHelper.getUser()
        val session = securityContextHolderHelper.getSession()

        val activeGameOfPlayer = gameSessionRepository.findPlayerActiveGameSession(session.sessionId!!)
        if (activeGameOfPlayer != null) {
            val activeGameId = activeGameOfPlayer.game?.id
            return JoinGameResponse(
                error = "You are already in a game.",
                activeGameId = activeGameId!!
            )
        }

        val games: List<Game> =
            gameRepository.findByTypeAndStatus(joinGameRequest.gameType!!, GameStatus.STARTING)
        var freeGame = findFreeGame(games)

        if (freeGame == null) {
            freeGame = Game(
                createdAt = LocalDateTime.now(clock),
                type = joinGameRequest.gameType,
                status = GameStatus.STARTING
            )
        } else {
            freeGame.status = GameStatus.IN_PROGRESS
        }

        gameRepository.save(freeGame)

        val gameSession = GameSession(
            game = freeGame,
            session = session,
            player = user,
            playerOptions = joinGameRequest.playerOptions
        )

        gameSessionRepository.save(gameSession)

        return JoinGameResponse(gameId = freeGame.id!!)
    }

    private fun findFreeGame(games: List<Game>): Game? {
        for (game in games) {
            val gamePlayers = gameSessionService.getGamePlayers(game)
            if (gamePlayers.opponentSession == null) {
                return game
            }
        }

        return null
    }
}
