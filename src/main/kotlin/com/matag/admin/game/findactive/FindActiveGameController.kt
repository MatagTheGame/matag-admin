package com.matag.admin.game.findactive

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/game")
open class FindActiveGameController(
    private val findGameService: FindGameService
) {

    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    @GetMapping
    open fun findActiveGame(): ActiveGameResponse =
        findGameService.findActiveGame()
}
