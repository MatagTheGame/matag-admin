package com.matag.admin.game.cancel;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matag.admin.auth.SecurityContextHolderHelper;
import com.matag.admin.game.finish.FinishGameService;
import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.session.GamePlayers;
import com.matag.admin.game.session.GameSessionRepository;
import com.matag.admin.game.session.GameSessionService;
import com.matag.admin.session.MatagSession;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CancelGameService {
  private final SecurityContextHolderHelper securityContextHolderHelper;
  private final GameRepository gameRepository;
  private final GameSessionRepository gameSessionRepository;
  private final GameSessionService gameSessionService;
  private final FinishGameService finishGameService;

  @Transactional
  public void cancel(Long gameId) {
    var session = securityContextHolderHelper.getSession();
    var activeGameSession = gameSessionRepository.findPlayerActiveGameSession(session.sessionId);
    if (activeGameSession.isPresent()) {
      if (activeGameSession.get().getGame().getId().equals(gameId)) {
        var game = activeGameSession.get().getGame();
        var gamePlayers = gameSessionService.getGamePlayers(game);
        if (gamePlayers.getOpponentSession() == null) {
          gameSessionRepository.delete(gamePlayers.getPlayerSession());
          gameRepository.delete(game);

        } else {
          String winnerSessionId = findOpponentSessionId(gamePlayers, session);
          finishGameService.finishGame(game, gamePlayers, winnerSessionId);
        }
      }
    }
  }

  private String findOpponentSessionId(GamePlayers gamePlayers, MatagSession session) {
    if (gamePlayers.getPlayerSession().getSession().id.equals(session.id)) {
      return gamePlayers.getOpponentSession().getSession().sessionId;
    } else {
      return gamePlayers.getPlayerSession().getSession().sessionId;
    }
  }
}
