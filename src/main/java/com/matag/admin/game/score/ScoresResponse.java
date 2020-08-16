package com.matag.admin.game.score;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = ScoresResponse.ScoresResponseBuilder.class)
@Builder
public class ScoresResponse {
  List<ScoreResponse> scores;

  @JsonPOJOBuilder(withPrefix = "")
  public static class ScoresResponseBuilder {

  }
}
