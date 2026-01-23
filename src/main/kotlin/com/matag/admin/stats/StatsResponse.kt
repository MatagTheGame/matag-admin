package com.matag.admin.stats

data class StatsResponse(
    var totaNonGuestlUsers: Long = 0,
    var onlineUsers: List<String> = listOf(),
    var totalCards: Int = 0,
    var totalSets: Int = 0
)
