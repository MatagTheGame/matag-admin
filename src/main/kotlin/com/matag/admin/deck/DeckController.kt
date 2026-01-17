package com.matag.admin.deck

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/deck")
open class DeckController {
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    open fun decks(): List<Deck> =
        listOf()
}
