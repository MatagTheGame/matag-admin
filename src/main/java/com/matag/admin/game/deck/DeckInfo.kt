package com.matag.admin.game.deck

import com.matag.cards.Card
import com.matag.cards.properties.Color
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

data class DeckInfo(
    var cards: List<Card> = listOf()
)

data class DeckMetadata(
    var type: String? = null,
    var options: DeckMetadataOptions? = null
)

data class DeckMetadataOptions(
    var colors: Set<Color> = setOf()
)
