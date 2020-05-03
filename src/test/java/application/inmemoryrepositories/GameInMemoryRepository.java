package application.inmemoryrepositories;

import com.matag.admin.game.game.Game;
import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.game.GameStatusType;
import com.matag.admin.game.game.GameType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class GameInMemoryRepository extends AbstractInMemoryRepository<Game> implements GameRepository {
  @Override
  public List<Game> findByTypeAndStatus(GameType type, GameStatusType status) {
    return findAll().stream()
      .filter(g -> g.getType().equals(type))
      .filter(g -> g.getStatus().equals(status))
      .collect(toList());
  }

  @Override
  public List<Game> findOldGameByStatus(GameStatusType status, LocalDateTime now) {
    return findAll().stream()
      .filter(g -> g.getCreatedAt().isBefore(now))
      .filter(g -> g.getStatus().equals(status))
      .collect(toList());
  }
}
