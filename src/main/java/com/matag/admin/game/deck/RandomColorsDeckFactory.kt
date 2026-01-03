package com.matag.admin.game.deck

import com.matag.cards.Card
import com.matag.cards.Cards
import com.matag.cards.ability.type.AbilityType
import com.matag.cards.properties.Color
import com.matag.cards.properties.Type
import com.matag.cards.search.CardSearch
import com.matag.cards.sets.MtgSets
import lombok.AllArgsConstructor
import org.springframework.stereotype.Component
import java.util.Collections.nCopies

val ALL_SETS = "_ALL_"

@Component
@AllArgsConstructor
class RandomColorsDeckFactory(
    private val cards: Cards,
    private val sets: MtgSets
) {
    fun create(deckMetadata: DeckMetadataOptions): List<Card> {
        val cardsPool = getCardsPool(deckMetadata)

        // 24 Lands
        val basicLands = (1..22).map { getBasicLandForColor(deckMetadata.colors.random()) }
        val nonBasicLands = nCopies(2, getRandomNonBasicLandOfTheseColors(cardsPool, deckMetadata.colors))

        // 36 Spells
        val creatures = nCopies(4, get5RandomCreaturesForColors(cardsPool, deckMetadata.colors)).flatten()
        val nonCreaturesSpell = nCopies(4, get3RandomNonCreatureSpellsForColors(cardsPool, deckMetadata.colors)).flatten()
        val colorlessCards = nCopies(4, randomColorlessCard(cardsPool))

        return basicLands + nonBasicLands + creatures + nonCreaturesSpell + colorlessCards
    }

    private fun getCardsPool(deckMetadata: DeckMetadataOptions): Collection<Card> =
        if (deckMetadata.sets.contains(ALL_SETS) || deckMetadata.sets.isEmpty()) {
            cards.all()
        } else {
            deckMetadata.sets
                .map { sets.get(it) }
                .flatMap { it.cards }
                .map { cards.get(it) }
        }

    private fun getBasicLandForColor(color: Color) =
        when (color) {
            Color.WHITE -> cards.get("Plains")
            Color.BLUE -> cards.get("Island")
            Color.BLACK -> cards.get("Swamp")
            Color.RED -> cards.get("Mountain")
            Color.GREEN -> cards.get("Forest")
        }

    private fun getRandomNonBasicLandOfTheseColors(cardsPool: Collection<Card>, deckColors: Set<Color>) =
        CardSearch(cardsPool)
            .ofType(Type.LAND)
            .notOfType(Type.BASIC)
            .cards
            .filter { canGenerateManaUsefulForColors(it, deckColors) }
            .random()

    private fun get5RandomCreaturesForColors(cardsPool: Collection<Card>, deckColors: Set<Color>) =
        CardSearch(cardsPool)
            .ofOnlyAnyOfTheColors(deckColors)
            .ofType(Type.CREATURE)
            .cards
            .shuffled()
            .take(5)

    private fun get3RandomNonCreatureSpellsForColors(cardsPool: Collection<Card>, deckColors: Set<Color>) =
        CardSearch(cardsPool)
            .ofOnlyAnyOfTheColors(deckColors)
            .notOfType(Type.CREATURE)
            .notOfType(Type.LAND)
            .cards
            .shuffled()
            .take(3)

    private fun randomColorlessCard(cardsPool: Collection<Card>) =
        CardSearch(cardsPool)
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
