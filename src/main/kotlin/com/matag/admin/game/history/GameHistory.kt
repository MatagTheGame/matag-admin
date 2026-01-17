package com.matag.admin.game.history

import com.matag.admin.game.game.GameType
import com.matag.admin.game.game.GameUserResultType
import java.time.LocalDateTime

data class GameHistory(
    var gameId: Long? = null,
    var startedTime: LocalDateTime? = null,
    var finishedTime: LocalDateTime? = null,
    var type: GameType? = null,
    var result: GameUserResultType? = null,
    var player1Name: String? = null,
    var player1Options: String? = null,
    var player2Name: String? = null,
    var player2Options: String? = null
)
