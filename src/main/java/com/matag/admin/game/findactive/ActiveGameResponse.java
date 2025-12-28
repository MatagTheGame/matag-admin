package com.matag.admin.game.findactive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActiveGameResponse {
  Long gameId;
  LocalDateTime createdAt;
  String playerName;
  String playerOptions;
  String opponentName;
  String opponentOptions;
}
