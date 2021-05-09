package com.matag.admin.game.result;

import org.springframework.stereotype.Component;

import com.matag.admin.game.game.Game;
import com.matag.admin.game.game.GameResultType;
import com.matag.admin.game.game.GameUserResultType;
import com.matag.admin.game.session.GamePlayers;
import com.matag.admin.game.session.GameSessionService;
import com.matag.admin.user.MatagUser;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ResultService {
  private final GameSessionService gameSessionService;

  public GameResultType getResult(GamePlayers gamePlayers, String winnerSessionId) {
    if (gamePlayers.getPlayerSession().getSession().getSessionId().equals(winnerSessionId)) {
      return GameResultType.R1;
    } else {
      return GameResultType.R2;
    }
  }

  public GameUserResultType toUserResult(Game game, MatagUser user) {
    var gamePlayers = gameSessionService.getGamePlayers(game);

    switch (game.getResult()) {
      case R1:
        if (gamePlayers.getPlayerSession().getPlayer().getUsername().equals(user.getUsername())) {
          return GameUserResultType.WIN;
        } else {
          return GameUserResultType.LOST;
        }

      case RX:
        return GameUserResultType.DRAW;

      case R2:
        if (gamePlayers.getPlayerSession().getPlayer().getUsername().equals(user.getUsername())) {
          return GameUserResultType.LOST;
        } else {
          return GameUserResultType.WIN;
        }

      default:
        throw new RuntimeException("Could not transform " + game.getResult());
    }
  }
}
