package com.matag.admin.game.join

import com.matag.admin.game.game.GameType

open class JoinGameRequest(
    var gameType: GameType? = null,
    var playerOptions: String? = null
)
