package com.matag.adminentities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.matag.cards.properties.Color;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = DeckMetadata.DeckMetadataBuilder.class)
@Builder(toBuilder = true)
public class DeckMetadata {
  Set<Color> randomColors;

  @JsonPOJOBuilder(withPrefix = "")
  public static class DeckMetadataBuilder {

  }
}