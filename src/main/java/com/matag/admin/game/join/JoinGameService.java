package com.matag.admin.game.join;

import com.matag.admin.auth.SecurityContextHolderHelper;
import com.matag.admin.game.game.Game;
import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;
import com.matag.admin.game.session.GameSessionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static com.matag.admin.game.game.GameStatusType.IN_PROGRESS;
import static com.matag.admin.game.game.GameStatusType.STARTING;

@Component
@AllArgsConstructor
public class JoinGameService {
  private final SecurityContextHolderHelper securityContextHolderHelper;
  private final GameRepository gameRepository;
  private final GameSessionRepository gameSessionRepository;
  private final GameSessionService gameSessionService;
  private final Clock clock;

  @Transactional
  public JoinGameResponse joinGame(JoinGameRequest joinGameRequest) {
    var user = securityContextHolderHelper.getUser();
    var session = securityContextHolderHelper.getSession();

    var activeGameOfPlayer = gameSessionRepository.findPlayerActiveGameSession(session.getSessionId());
    if (activeGameOfPlayer.isPresent()) {
      var activeGameId = activeGameOfPlayer.get().getGame().getId();
      return new JoinGameResponse(null, "You are already in a game.", activeGameId);
    }

    var games = gameRepository.findByTypeAndStatus(joinGameRequest.gameType(), STARTING);
    var freeGame = findFreeGame(games);

    if (freeGame == null) {
      freeGame = Game.builder()
        .createdAt(LocalDateTime.now(clock))
        .type(joinGameRequest.gameType())
        .status(STARTING)
        .build();
    } else {
      freeGame.setStatus(IN_PROGRESS);
    }

    gameRepository.save(freeGame);

    var gameSession = GameSession.builder()
      .game(freeGame)
      .session(session)
      .player(user)
      .playerOptions(joinGameRequest.playerOptions())
      .build();

    gameSessionRepository.save(gameSession);

    return new JoinGameResponse(freeGame.getId(), null, null);
  }

  private Game findFreeGame(List<Game> games) {
    for (Game game : games) {
      var gamePlayers = gameSessionService.getGamePlayers(game);
      if (gamePlayers.getOpponentSession() == null) {
        return game;
      }
    }

    return null;
  }
}
