package com.matag.admin.game.join;

import static com.matag.admin.game.game.GameStatusType.IN_PROGRESS;
import static com.matag.admin.game.game.GameStatusType.STARTING;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matag.admin.auth.SecurityContextHolderHelper;
import com.matag.admin.game.game.Game;
import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;
import com.matag.admin.game.session.GameSessionService;

import lombok.AllArgsConstructor;

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

    var activeGameOfPlayer = gameSessionRepository.findPlayerActiveGameSession(session.sessionId);
    if (activeGameOfPlayer.isPresent()) {
      var activeGameId = activeGameOfPlayer.get().getGame().getId();
      return JoinGameResponse.builder()
        .error("You are already in a game.")
        .activeGameId(activeGameId)
        .build();
    }

    var games = gameRepository.findByTypeAndStatus(joinGameRequest.getGameType(), STARTING);
    var freeGame = findFreeGame(games);

    if (freeGame == null) {
      freeGame = Game.builder()
        .createdAt(LocalDateTime.now(clock))
        .type(joinGameRequest.getGameType())
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
      .playerOptions(joinGameRequest.getPlayerOptions())
      .build();

    gameSessionRepository.save(gameSession);

    return JoinGameResponse.builder()
      .gameId(freeGame.getId())
      .build();
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
