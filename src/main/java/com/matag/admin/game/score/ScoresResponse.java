package com.matag.admin.game.score;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

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
