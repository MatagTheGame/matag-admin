package com.matag.admin.game.findactive

import java.time.LocalDateTime

data class ActiveGameResponse(
    var gameId: Long? = null,
    var createdAt: LocalDateTime? = null,
    var playerName: String? = null,
    var playerOptions: String? = null,
    var opponentName: String? = null,
    var opponentOptions: String? = null
)
