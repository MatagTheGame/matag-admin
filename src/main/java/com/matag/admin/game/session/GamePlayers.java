package com.matag.admin.game.session;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Value
public class GamePlayers {
  GameSession playerSession;
  GameSession opponentSession;


  public GameSession getPlayerSession() {
    return playerSession;
  }

  public GameSession getOpponentSession() {
    return opponentSession;
  }
}
