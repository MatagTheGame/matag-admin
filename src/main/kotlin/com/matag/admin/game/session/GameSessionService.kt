package com.matag.admin.game.session

import com.matag.admin.game.game.Game
import lombok.AllArgsConstructor
import org.springframework.stereotype.Component

@Component
open class GameSessionService {
    open fun getGamePlayers(game: Game): GamePlayers {
        val gameSessions: List<GameSession> = game.gameSessions

        if (gameSessions.size == 1) {
            return GamePlayers(gameSessions.get(0), null)
        } else if (gameSessions.size == 2) {
            if (gameSessions.get(0).id!! < gameSessions.get(1).id!!) {
                return GamePlayers(gameSessions.get(0), gameSessions.get(1))
            } else {
                return GamePlayers(gameSessions.get(1), gameSessions.get(0))
            }
        }

        return GamePlayers(null, null)
    }
}
