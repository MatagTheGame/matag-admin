package com.matag.admin.game.deck;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matag.admin.auth.SecurityContextHolderHelper;
import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;
import com.matag.adminentities.DeckInfo;
import com.matag.cards.Card;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
@RestController
@RequestMapping("/game/active-deck")
public class DeckRetrieverController {
  private final GameSessionRepository gameSessionRepository;
  private final SecurityContextHolderHelper securityContextHolderHelper;
  private final ObjectMapper objectMapper;
  private final RandomColorsDeckFactory randomColorsDeckFactory;

  @GetMapping
  public DeckInfo deckInfo() {
    var sessionId = securityContextHolderHelper.getSession().getSessionId();
    var activeSession = gameSessionRepository.findPlayerActiveGameSession(sessionId)
      .orElseThrow(() -> new RuntimeException("Active deck not found."));
    DeckMetadata deckMetadata = readDeckInfo(activeSession);
    return buildDeck(deckMetadata);
  }

  private DeckInfo buildDeck(DeckMetadata deckMetadata) {
    List<Card> cards = switch (deckMetadata.getType()) {
      case "random" -> randomColorsDeckFactory.create(deckMetadata.getOptions());
      case "pre-constructed" -> throw new RuntimeException("pre-constructed not implemented.");
      case "custom" -> throw new RuntimeException("pre-constructed not implemented.");
      default -> throw new RuntimeException(deckMetadata.getType() + " not recognised.");
    };

    return DeckInfo.builder().cards(cards).build();
  }

  @SneakyThrows
  private DeckMetadata readDeckInfo(GameSession gs) {
    return objectMapper.readValue(gs.getPlayerOptions(), DeckMetadata.class);
  }
}
