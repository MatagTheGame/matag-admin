package com.matag.admin.stats

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stats")
open class StatsController(
    private val statsService: StatsService
) {

    @PreAuthorize("permitAll()")
    @GetMapping
    open fun stats(): StatsResponse {
        return StatsResponse(
            totaNonGuestlUsers = statsService.countTotalUsers() - 1,
            onlineUsers = statsService.onlineUsers(),
            totalCards = statsService.countCards(),
            totalSets = statsService.countSets()
        )
    }
}
