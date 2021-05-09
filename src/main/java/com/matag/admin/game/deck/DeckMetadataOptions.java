package com.matag.admin.game.deck;

import java.util.Set;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.matag.cards.properties.Color;

import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = DeckMetadataOptions.DeckMetadataOptionsBuilder.class)
@Builder(toBuilder = true)
public class DeckMetadataOptions {
  Set<Color> colors;

  @JsonPOJOBuilder(withPrefix = "")
  public static class DeckMetadataOptionsBuilder {

  }
}