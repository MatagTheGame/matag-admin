package com.matag.admin.game.history;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.matag.admin.game.game.GameType;
import com.matag.admin.game.game.GameUserResultType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = GameHistory.GameHistoryBuilder.class)
@Builder(toBuilder = true)
public class GameHistory {
  LocalDateTime startedTime;
  LocalDateTime finishedTime;
  GameType type;
  GameUserResultType result;
  String player1Name;
  String player1Options;
  String player2Name;
  String player2Options;

  @JsonPOJOBuilder(withPrefix = "")
  public static class GameHistoryBuilder {

  }
}
