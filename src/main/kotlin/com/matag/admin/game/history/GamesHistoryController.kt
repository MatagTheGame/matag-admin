package com.matag.admin.game.history

import com.matag.admin.auth.SecurityContextHolderHelper
import com.matag.admin.game.game.Game
import com.matag.admin.game.game.GameRepository
import com.matag.admin.game.game.GameStatus
import com.matag.admin.game.result.ResultService
import com.matag.admin.game.session.GameSessionService
import com.matag.admin.user.MatagUser
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/game")
open class GamesHistoryController(
    private val securityContextHolderHelper: SecurityContextHolderHelper,
    private val gameRepository: GameRepository,
    private val gameSessionService: GameSessionService,
    private val resultService: ResultService
) {

    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    @GetMapping("/history")
    open fun gameHistory(): GamesHistoryResponse? {
        val user = securityContextHolderHelper.getUser()
        val games = gameRepository.findByPlayerIdAndStatus(user.id!!, GameStatus.FINISHED)
        return GamesHistoryResponse(
            gamesHistory = games.map { this.toGameHistory(it!!, user) }
        )
    }

    private fun toGameHistory(game: Game, user: MatagUser): GameHistory {
        val gamePlayers = gameSessionService!!.getGamePlayers(game)
        val player1 = gamePlayers.playerSession
        val player2 = gamePlayers.opponentSession
        return GameHistory(
            gameId = game.id,
            startedTime = game.createdAt,
            finishedTime = game.finishedAt,
            type = game.type,
            result = resultService!!.toUserResult(game, user),
            player1Name = player1!!.player!!.username,
            player1Options = player1.playerOptions,
            player2Name = player2!!.player!!.username,
            player2Options = player2.playerOptions,
        )
    }
}
