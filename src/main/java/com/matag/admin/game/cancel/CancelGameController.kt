package com.matag.admin.game.cancel

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/game")
open class CancelGameController(
    private val cancelGameService: CancelGameService
) {
    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    @DeleteMapping("/{id}")
    open fun cancelGame(@PathVariable("id") gameId: Long): Boolean {
        cancelGameService.cancel(gameId)
        return true
    }
}
