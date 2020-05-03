package application.inmemoryrepositories;

import com.matag.admin.game.game.Game;
import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.matag.admin.game.game.GameStatusType.IN_PROGRESS;
import static com.matag.admin.game.game.GameStatusType.STARTING;
import static java.util.stream.Collectors.toList;

@Component
public class GameSessionAbstractInMemoryRepository extends AbstractInMemoryRepository<GameSession> implements GameSessionRepository {
  @Override
  public List<GameSession> findByGame(Game game) {
    return findAll().stream()
      .filter(gs -> gs.getGame().getId().equals(game.getId()))
      .collect(toList());
  }

  @Override
  public Optional<GameSession> findPlayerActiveGameSession(String session) {
    return findAll().stream()
      .filter(gs -> gs.getSession().getSessionId().equals(session))
      .filter(gs -> gs.getGame().getStatus() == STARTING || gs.getGame().getStatus() == IN_PROGRESS)
      .findFirst();
  }
}
