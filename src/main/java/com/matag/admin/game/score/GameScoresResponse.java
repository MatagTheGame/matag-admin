package com.matag.admin.game.score;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = GameScoresResponse.GamesScoreResponseBuilder.class)
@Builder
public class GameScoresResponse {
  List<GameScore> gameScores;

  @JsonPOJOBuilder(withPrefix = "")
  public static class GamesScoreResponseBuilder {

  }
}
