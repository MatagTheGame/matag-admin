package com.matag.admin.game.deck

import com.matag.cards.Card
import com.matag.cards.Cards
import com.matag.cards.ability.Ability
import com.matag.cards.ability.type.AbilityType
import com.matag.cards.properties.Color
import com.matag.cards.properties.Type
import com.matag.cards.search.CardSearch
import lombok.AllArgsConstructor
import org.springframework.stereotype.Component
import java.util.*
import java.util.Collections.nCopies
import java.util.stream.Collectors

@Component
@AllArgsConstructor
class RandomColorsDeckFactory(
    private val cards: Cards
) {
    fun create(deckMetadata: DeckMetadataOptions): List<Card> {
        // 24 Lands
        val basicLands = (1..22).map { getBasicLandForColor(deckMetadata.colors.random()) }
        val nonBasicLands = nCopies(2, getRandomNonBasicLandOfTheseColors(deckMetadata.colors))

        // 36 Spells
        val creatures = nCopies(4, get5RandomCreaturesForColors(deckMetadata.colors)).flatten()
        val nonCreaturesSpell = nCopies(4, get3RandomNonCreatureSpellsForColors(deckMetadata.colors)).flatten()
        val colorlessCards = nCopies(4, randomColorlessCard())

        return basicLands + nonBasicLands + creatures + nonCreaturesSpell + colorlessCards
    }

    private fun getBasicLandForColor(color: Color) =
        when (color) {
            Color.WHITE -> cards.get("Plains")
            Color.BLUE -> cards.get("Island")
            Color.BLACK -> cards.get("Swamp")
            Color.RED -> cards.get("Mountain")
            Color.GREEN -> cards.get("Forest")
        }

    private fun getRandomNonBasicLandOfTheseColors(deckColors: Set<Color>) =
        CardSearch(cards.all())
            .ofType(Type.LAND)
            .notOfType(Type.BASIC)
            .cards
            .filter { canGenerateManaUsefulForColors(it, deckColors) }
            .random()

    private fun get5RandomCreaturesForColors(deckColors: Set<Color>) =
        CardSearch(cards.all())
            .ofOnlyAnyOfTheColors(deckColors)
            .ofType(Type.CREATURE)
            .cards
            .shuffled()
            .take(5)

    private fun get3RandomNonCreatureSpellsForColors(deckColors: Set<Color>) =
        CardSearch(cards.all().stream().toList())
            .ofOnlyAnyOfTheColors(deckColors)
            .notOfType(Type.CREATURE)
            .notOfType(Type.LAND)
            .cards
            .shuffled()
            .take(3)

    private fun randomColorlessCard() =
        CardSearch(cards.all())
            .notOfType(Type.LAND)
            .colorless()
            .cards
            .random()

    private fun canGenerateManaUsefulForColors(card: Card, deckColors: Set<Color>): Boolean =
        card.abilities
            .filter { it.abilityType == AbilityType.TAP_ADD_MANA }
            .flatMap { it.parameters }
            .any { it == "COLORLESS" || deckColors.contains(Color.valueOf(it)) }
}
