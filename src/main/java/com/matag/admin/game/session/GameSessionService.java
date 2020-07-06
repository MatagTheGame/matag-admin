package com.matag.admin.game.session;

import com.matag.admin.game.game.Game;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class GameSessionService {
  public GamePlayers getGamePlayers(Game game) {
    var gameSessions = game.getGameSessions();

    if (gameSessions.size() == 1) {
      return new GamePlayers(gameSessions.get(0), null);

    } else if (gameSessions.size() == 2) {
      if (gameSessions.get(0).getId() < gameSessions.get(1).getId()) {
        return new GamePlayers(gameSessions.get(0), gameSessions.get(1));

      } else {
        return new GamePlayers(gameSessions.get(1), gameSessions.get(0));
      }
    }

    return new GamePlayers(null, null);
  }
}
