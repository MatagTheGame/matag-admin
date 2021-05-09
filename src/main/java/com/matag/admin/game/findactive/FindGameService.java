package com.matag.admin.game.findactive;

import org.springframework.stereotype.Component;

import com.matag.admin.auth.SecurityContextHolderHelper;
import com.matag.admin.game.session.GameSessionRepository;
import com.matag.admin.game.session.GameSessionService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class FindGameService {
  private final SecurityContextHolderHelper securityContextHolderHelper;
  private final GameSessionRepository gameSessionRepository;
  private final GameSessionService gameSessionService;

  public ActiveGameResponse findActiveGame() {
    var session = securityContextHolderHelper.getSession();
    var activeGameSession = gameSessionRepository.findPlayerActiveGameSession(session.getSessionId());

    if (activeGameSession.isEmpty()) {
      return ActiveGameResponse.builder().build();
    }

    var game = activeGameSession.get().getGame();
    var gamePlayers = gameSessionService.getGamePlayers(game);

    return ActiveGameResponse.builder()
      .gameId(game.getId())
      .createdAt(game.getCreatedAt())
      .playerName(gamePlayers.getPlayerSession().getPlayer().getUsername())
      .playerOptions(gamePlayers.getPlayerSession().getPlayerOptions())
      .opponentName(gamePlayers.getOpponentSession() != null ? gamePlayers.getOpponentSession().getPlayer().getUsername() : null)
      .opponentOptions(gamePlayers.getOpponentSession() != null ? gamePlayers.getOpponentSession().getPlayerOptions() : null)
      .build();
  }
}
