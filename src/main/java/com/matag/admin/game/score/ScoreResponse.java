package com.matag.admin.game.score;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScoreResponse {
  Integer rank;
  String player;
  Integer elo;
  Integer wins;
  Integer draws;
  Integer losses;
}
