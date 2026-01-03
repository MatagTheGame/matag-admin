package com.matag.admin.game.deck;

import static com.matag.cards.ability.type.AbilityType.TAP_ADD_MANA;
import static java.util.Collections.nCopies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.matag.cards.Card;
import com.matag.cards.Cards;
import com.matag.cards.properties.Color;
import com.matag.cards.properties.Type;
import com.matag.cards.search.CardSearch;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RandomColorsDeckFactory {
  private final Cards cards;

  public List<Card> create(DeckMetadataOptions deckMetadata) {
    var randomColors = deckMetadata.getColors();
    var cards = new ArrayList<Card>();

    // Lands
    for (var i = 0; i < 22; i++) {
      var color = new ArrayList<>(randomColors).get(i % randomColors.size());
      cards.add(getBasicLandForColor(color));
    }
    cards.addAll(nCopies(2, getRandomNonBasicLandOfTheseColors(randomColors)));

    // Spells
    for (var randomCard : getRandomSpellsForColors(randomColors)) {
      cards.addAll(nCopies(4, randomCard));
    }
    cards.addAll(nCopies(4, getRandomColorlessCard()));

    Collections.shuffle(cards);

    return cards;
  }

  private Card getBasicLandForColor(Color color) {
    return switch (color) {
      case WHITE -> cards.get("Plains");
      case BLUE -> cards.get("Island");
      case BLACK -> cards.get("Swamp");
      case RED -> cards.get("Mountain");
      case GREEN -> cards.get("Forest");
    };
  }

  private List<Card> getRandomSpellsForColors(Set<Color> deckColors) {
    var selectedCards = new ArrayList<Card>();

    var creatureCardsOfTheseColors = new CardSearch(cards.all().stream().toList())
      .ofOnlyAnyOfTheColors(deckColors)
      .ofType(Type.CREATURE)
      .getCards();
    Collections.shuffle(creatureCardsOfTheseColors);
    selectedCards.addAll(creatureCardsOfTheseColors.subList(0, 5));

    var nonCreatureCardsOfTheseColors = new CardSearch(cards.all().stream().toList())
      .ofOnlyAnyOfTheColors(deckColors)
      .notOfType(Type.CREATURE)
      .getCards();
    Collections.shuffle(nonCreatureCardsOfTheseColors);
    selectedCards.addAll(nonCreatureCardsOfTheseColors.subList(0, 3));

    return selectedCards;
  }

  private Card getRandomNonBasicLandOfTheseColors(Set<Color> deckColors) {
    var nonBasicLands = new CardSearch(cards.all().stream().toList())
      .ofType(Type.LAND)
      .notOfType(Type.BASIC)
      .getCards();

    var nonBasicLandsMatchingBothColors = nonBasicLands.stream()
      .filter(card -> matchesColors(card, deckColors))
      .collect(Collectors.toList());

    if (!nonBasicLandsMatchingBothColors.isEmpty()) {
      Collections.shuffle(nonBasicLandsMatchingBothColors);
      return nonBasicLandsMatchingBothColors.get(0);

    } else {
      var nonBasicLandsMatchingOneColor = nonBasicLands.stream()
        .filter(card -> matchesOneColor(card, deckColors))
        .collect(Collectors.toList());

      Collections.shuffle(nonBasicLandsMatchingOneColor);
      return nonBasicLandsMatchingOneColor.get(0);
    }
  }

  private Card getRandomColorlessCard() {
    var allColorlessCards = new CardSearch(cards.all().stream().toList())
      .colorless()
      .getCards();
    Collections.shuffle(allColorlessCards);

    return allColorlessCards.get(0);
  }

  private boolean matchesColors(Card card, Set<Color> deckColors) {
    var cardColors = colorsOfManaThatCanGenerate(card);
    return deckColors.containsAll(cardColors);
  }

  private boolean matchesOneColor(Card card, Set<Color> deckColors) {
    var cardColors = colorsOfManaThatCanGenerate(card);
    for (var deckColor : deckColors) {
      for (var cardColor : cardColors) {
        if (deckColor == cardColor) {
          return true;
        }
      }
    }

    return false;
  }

  private Set<Color> colorsOfManaThatCanGenerate(Card card) {
    return card.getAbilities().stream()
      .filter(ability -> ability.getAbilityType().equals(TAP_ADD_MANA))
      .flatMap(ability -> ability.getParameters().stream())
      .filter(cost -> !cost.equals("COLORLESS"))
      .map(Color::valueOf)
      .collect(Collectors.toSet());
  }
}
