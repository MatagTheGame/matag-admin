package com.matag.admin.game.finish

import com.matag.admin.game.finish.FinishGameRequest
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/game")
open class FinishGameController(
    private val finishGameService: FinishGameService
) {

    @PostMapping("/{id}/finish")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    open fun finish(@PathVariable("id") gameId: Long, @RequestBody request: FinishGameRequest) {
        finishGameService.finish(gameId, request)
    }
}
