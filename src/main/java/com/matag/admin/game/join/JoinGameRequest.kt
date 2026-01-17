package com.matag.admin.game.join

import com.matag.admin.game.game.GameType
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

open class JoinGameRequest(
    var gameType: GameType? = null,
    var playerOptions: String? = null
)
