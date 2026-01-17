package com.matag.admin.game.score

data class ScoreResponse(
    var rank: Int? = null,
    var player: String? = null,
    var elo: Int? = null,
    var wins: Int? = null,
    var draws: Int? = null,
    var losses: Int? = null
)
