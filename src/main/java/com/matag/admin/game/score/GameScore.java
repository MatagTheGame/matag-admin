package com.matag.admin.game.score;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = GameScore.GameScoreBuilder.class)
@Builder
public class GameScore {
  Long rank;
  String player;
  Integer elo;
  Long wins;
  Long draws;
  Long losses;

  @JsonPOJOBuilder(withPrefix = "")
  public static class GameScoreBuilder {

  }
}
