package application.inmemoryrepositories;

import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.matag.admin.game.game.GameStatusType.IN_PROGRESS;
import static com.matag.admin.game.game.GameStatusType.STARTING;

@Component
public class GameSessionAbstractInMemoryRepository extends AbstractInMemoryRepository<GameSession> implements GameSessionRepository {
  @Override
  public Optional<GameSession> findPlayerActiveGameSession(String session) {
    return findAll().stream()
      .filter(gs -> gs.getSession().getSessionId().equals(session))
      .filter(gs -> gs.getGame().getStatus() == STARTING || gs.getGame().getStatus() == IN_PROGRESS)
      .findFirst();
  }
}
