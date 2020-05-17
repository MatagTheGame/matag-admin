package com.matag.admin.game.history;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = GamesHistoryResponse.GamesHistoryResponseBuilder.class)
@Builder(toBuilder = true)
public class GamesHistoryResponse {
  List<GameHistory> gamesHistory;

  @JsonPOJOBuilder(withPrefix = "")
  public static class GamesHistoryResponseBuilder {

  }
}
