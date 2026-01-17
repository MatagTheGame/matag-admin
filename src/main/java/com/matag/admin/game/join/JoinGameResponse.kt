package com.matag.admin.game.join

import lombok.Data

@Data
data class JoinGameResponse(
    var gameId: Long = 0,
    var error: String = "",
    var activeGameId: Long = 0
)
