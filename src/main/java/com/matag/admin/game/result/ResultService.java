package com.matag.admin.game.result;

import com.matag.admin.game.game.GameResultType;
import com.matag.admin.game.game.GameUserResultType;
import com.matag.admin.game.session.GamePlayers;
import com.matag.admin.user.MatagUser;
import org.springframework.stereotype.Component;

@Component
public class ResultService {
  public GameResultType getResult(GamePlayers gamePlayers, String winnerSessionId) {
    if (gamePlayers.getPlayerSession().getSession().getSessionId().equals(winnerSessionId)) {
      return GameResultType.R1;
    } else {
      return GameResultType.R2;
    }
  }

  public GameUserResultType toUserResult(GamePlayers loadPlayers, MatagUser user) {
    return null;
  }
}
