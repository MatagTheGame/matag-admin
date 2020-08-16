package com.matag.admin.game.score;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.matag.admin.game.game.GameType;
import com.matag.admin.game.history.GamesHistoryResponse;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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
