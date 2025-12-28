package com.matag.admin.game.deck;

import com.matag.cards.properties.Color;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DeckMetadataOptions {
  Set<Color> colors;
}