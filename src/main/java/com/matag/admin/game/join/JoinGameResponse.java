package com.matag.admin.game.join;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JoinGameResponse(
  @JsonProperty("gameId")Long gameId,
  @JsonProperty("error")String error,
  @JsonProperty("activeGameId")Long activeGameId) {
}
