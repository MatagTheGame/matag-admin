package com.matag.admin.game.join;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.matag.admin.game.game.GameType;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record JoinGameRequest(GameType gameType, String playerOptions) {
}
