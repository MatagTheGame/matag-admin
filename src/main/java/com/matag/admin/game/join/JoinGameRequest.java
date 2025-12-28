package com.matag.admin.game.join;

import com.matag.admin.game.game.GameType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinGameRequest {
  GameType gameType;
  String playerOptions;
}
