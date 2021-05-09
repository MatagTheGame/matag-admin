package com.matag.admin.game.finish;

import static com.matag.admin.game.game.GameStatusType.IN_PROGRESS;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matag.admin.game.game.Game;
import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.game.GameStatusType;
import com.matag.admin.game.result.ResultService;
import com.matag.admin.game.score.EloApplyService;
import com.matag.admin.game.session.GamePlayers;
import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;
import com.matag.admin.game.session.GameSessionService;
import com.matag.adminentities.FinishGameRequest;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class FinishGameService {
  private final Clock clock;
  private final GameRepository gameRepository;
  private final GameSessionRepository gameSessionRepository;
  private final GameSessionService gameSessionService;
  private final ResultService resultService;
  private final EloApplyService eloApplyService;

  @Transactional
  public void finish(Long gameId, FinishGameRequest request) {
    var gameOptional = gameRepository.findById(gameId);
    gameOptional.ifPresent(game -> {
      if (game.getStatus() == IN_PROGRESS) {
        var gamePlayers = gameSessionService.getGamePlayers(game);
        finishGame(game, gamePlayers, request.getWinnerSessionId());
      }
    });
  }

  public void finishGame(Game game, GamePlayers gamePlayers, String winnerSessionId) {
    GameSession session1 = gamePlayers.getPlayerSession();
    GameSession session2 = gamePlayers.getOpponentSession();

    var gameResultType = resultService.getResult(gamePlayers, winnerSessionId);
    game.setStatus(GameStatusType.FINISHED);
    game.setResult(gameResultType);
    game.setFinishedAt(LocalDateTime.now(clock));
    gameRepository.save(game);

    gamePlayers.getPlayerSession().setSession(null);
    gameSessionRepository.save(session1);
    gamePlayers.getOpponentSession().setSession(null);
    gameSessionRepository.save(session2);

    eloApplyService.apply(session1.getPlayer(), session2.getPlayer(), gameResultType);
  }
}
