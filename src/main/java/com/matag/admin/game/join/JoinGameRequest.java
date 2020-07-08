package com.matag.admin.game.join;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.matag.admin.game.game.GameType;

public record JoinGameRequest(
  @JsonProperty("gameType")GameType gameType,
  @JsonProperty("playerOptions")String playerOptions) {
}
