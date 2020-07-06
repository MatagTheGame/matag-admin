package com.matag.admin.cleanup;

import com.matag.admin.game.game.Game;
import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static com.matag.admin.game.game.GameStatusType.IN_PROGRESS;
import static com.matag.admin.game.game.GameStatusType.STARTING;

@Component
@AllArgsConstructor
public class MatagGameCleanup {
  private static final Logger LOGGER = LoggerFactory.getLogger(MatagGameCleanup.class);

  private final GameRepository gameRepository;
  private final GameSessionRepository gameSessionRepository;
  private final Clock clock;

  @Transactional
  public void cleanup() {
    var oldNotFinishedGames = gameRepository.findOldGameByStatus(List.of(STARTING, IN_PROGRESS), LocalDateTime.now(clock).minusDays(2));
    LOGGER.info("MatagGameCleanup identified " + oldNotFinishedGames.size() + " entries to delete.");
    var totalGameDeleted = 0;
    for (Game game : oldNotFinishedGames) {
      try {
        for (GameSession gameSession : game.getGameSessions()) {
          gameSessionRepository.delete(gameSession);
          gameRepository.delete(game);
          totalGameDeleted++;
        }

      } catch (Exception e) {
        LOGGER.error("Could not delete game with id " + game.getId(), e);
      }
    }

    LOGGER.info("MatagGameCleanup deleted " + totalGameDeleted + " games");
  }
}
