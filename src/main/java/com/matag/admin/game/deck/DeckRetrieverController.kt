package com.matag.admin.game.deck

import com.fasterxml.jackson.databind.ObjectMapper
import com.matag.admin.auth.SecurityContextHolderHelper
import com.matag.admin.game.session.GameSession
import com.matag.admin.game.session.GameSessionRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/game/active-deck")
open class DeckRetrieverController(
    val gameSessionRepository: GameSessionRepository,
    val securityContextHolderHelper: SecurityContextHolderHelper,
    val objectMapper: ObjectMapper,
    val randomColorsDeckFactory: RandomColorsDeckFactory
) {
    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    @GetMapping
    open fun deckInfo(): DeckInfo {
        val activeSession = gameSessionRepository.findPlayerActiveGameSession(securityContextHolderHelper.getSession().sessionId)
            .orElseThrow { IllegalStateException("Active deck not found.") }
        val deckMetadata = readDeckInfo(activeSession)
        return buildDeck(deckMetadata)
    }

    private fun buildDeck(deckMetadata: DeckMetadata): DeckInfo {
        val cards = when (deckMetadata.type) {
            "random" -> randomColorsDeckFactory.create(requireNotNull(deckMetadata.options) { "deckMetadata.options required for random deck" })
            "pre-constructed" -> throw NotImplementedError("pre-constructed not implemented.")
            "custom" -> throw NotImplementedError("custom not implemented.")
            else -> throw IllegalStateException(deckMetadata.type + " not recognised.")
        }

        return DeckInfo(cards)
    }

    private fun readDeckInfo(gameSession: GameSession): DeckMetadata {
        return objectMapper.readValue(gameSession.playerOptions, DeckMetadata::class.java)
    }
}
