package com.matag.admin.game.deck

import com.matag.cards.Card
import com.matag.cards.properties.Color

data class DeckInfo(
    var cards: List<Card> = listOf()
)

data class DeckMetadata(
    var type: String? = null,
    var options: DeckMetadataOptions? = null
)

data class DeckMetadataOptions(
    var sets: Set<String> = setOf(),
    var colors: Set<Color> = setOf()
)
