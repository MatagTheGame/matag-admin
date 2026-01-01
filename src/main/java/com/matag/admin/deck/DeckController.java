package com.matag.admin.deck;

import static java.util.Collections.emptyList;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deck")
public class DeckController {

  @PreAuthorize("hasRole('USER')")
  @GetMapping
  public List<Deck> decks() {
    return emptyList();
  }
}
