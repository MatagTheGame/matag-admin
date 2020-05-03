package application.inmemoryrepositories;

import com.matag.admin.session.MatagSession;
import com.matag.admin.session.MatagSessionRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Component
public class MatagSessionAbstractInMemoryRepository extends AbstractInMemoryRepository<MatagSession> implements MatagSessionRepository {
  @Override
  public Optional<MatagSession> findBySessionId(String sessionId) {
    return DATA.values().stream()
      .filter(s -> s.getSessionId().equals(sessionId))
      .findFirst();
  }

  @Override
  public long countOnlineUsers(LocalDateTime now) {
    return DATA.values().stream()
      .filter(s -> s.getValidUntil().isAfter(now))
      .count();
  }

  @Override
  public int deleteValidUntilBefore(LocalDateTime now) {
    List<Long> keysToRemove = DATA.entrySet().stream()
      .filter(e -> e.getValue().getValidUntil().isAfter(now))
      .map(Map.Entry::getKey)
      .collect(toList());
    int size = keysToRemove.size();

    keysToRemove.forEach(DATA::remove);

    return size;
  }

  @Override
  public void deleteBySessionId(String sessionId) {
    Optional<MatagSession> session = findBySessionId(sessionId);
    session.ifPresent(s -> deleteById(s.getId()));
  }
}
