package com.matag.admin.game.join;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinGameResponse {
  Long gameId;
  String error;
  Long activeGameId;
}
