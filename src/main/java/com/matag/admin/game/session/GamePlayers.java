package com.matag.admin.game.session;

import lombok.Value;

@Value
public class GamePlayers {
  GameSession playerSession;
  GameSession opponentSession;
}
