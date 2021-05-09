package com.matag.admin.game.deck;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = DeckMetadata.DeckMetadataBuilder.class)
@Builder(toBuilder = true)
public class DeckMetadata {
  String type;
  DeckMetadataOptions options;

  @JsonPOJOBuilder(withPrefix = "")
  public static class DeckMetadataBuilder {

  }
}