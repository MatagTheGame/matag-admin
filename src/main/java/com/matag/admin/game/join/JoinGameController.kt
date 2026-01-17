package com.matag.admin.game.join

import lombok.AllArgsConstructor
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/game")
open class JoinGameController(
    private val joinGameService: JoinGameService
) {

    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    @PostMapping
    open fun joinGame(@RequestBody joinGameRequest: JoinGameRequest): JoinGameResponse =
        joinGameService.joinGame(joinGameRequest)
}
