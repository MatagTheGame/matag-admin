package com.matag.admin.game.score;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = ScoreResponse.ScoreResponseBuilder.class)
@Builder
public class ScoreResponse {
  Integer rank;
  String player;
  Integer elo;
  Integer wins;
  Integer draws;
  Integer losses;

  @JsonPOJOBuilder(withPrefix = "")
  public static class ScoreResponseBuilder {

  }
}
