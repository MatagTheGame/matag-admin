package com.matag.admin.game.history;

import com.matag.admin.game.game.GameType;
import com.matag.admin.game.game.GameUserResultType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameHistory {
  Long gameId;
  LocalDateTime startedTime;
  LocalDateTime finishedTime;
  GameType type;
  GameUserResultType result;
  String player1Name;
  String player1Options;
  String player2Name;
  String player2Options;
}
