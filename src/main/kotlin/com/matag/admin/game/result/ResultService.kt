package com.matag.admin.game.result

import com.matag.admin.game.game.Game
import com.matag.admin.game.game.GameResultType
import com.matag.admin.game.game.GameUserResultType
import com.matag.admin.game.session.GamePlayers
import com.matag.admin.game.session.GameSessionService
import com.matag.admin.user.MatagUser
import org.springframework.stereotype.Component

@Component
class ResultService(
    private val gameSessionService: GameSessionService
) {
    fun getResult(gamePlayers: GamePlayers, winnerSessionId: String?): GameResultType {
        if (gamePlayers.playerSession!!.session!!.sessionId == winnerSessionId) {
            return GameResultType.R1
        } else {
            return GameResultType.R2
        }
    }

    fun toUserResult(game: Game, user: MatagUser): GameUserResultType {
        val gamePlayers = gameSessionService.getGamePlayers(game)

        when (game.result) {
            GameResultType.R1 -> if (gamePlayers.playerSession!!.player!!.username == user.username) {
                return GameUserResultType.WIN
            } else {
                return GameUserResultType.LOST
            }

            GameResultType.RX -> return GameUserResultType.DRAW

            GameResultType.R2 -> if (gamePlayers.playerSession!!.player!!.username == user.username) {
                return GameUserResultType.LOST
            } else {
                return GameUserResultType.WIN
            }

            else -> throw RuntimeException("Could not transform " + game.result)
        }
    }
}
