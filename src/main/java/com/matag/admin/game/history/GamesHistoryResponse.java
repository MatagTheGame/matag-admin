package com.matag.admin.game.history;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = GamesHistoryResponse.GamesHistoryResponseBuilder.class)
@Builder
public class GamesHistoryResponse {
  List<GameHistory> gamesHistory;

  @JsonPOJOBuilder(withPrefix = "")
  public static class GamesHistoryResponseBuilder {

  }
}
