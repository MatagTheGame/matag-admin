package com.matag.admin.game.session;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameSessionRepository extends CrudRepository<GameSession, Long> {
  @Query("FROM GameSession gs WHERE gs.session.sessionId = ?1 AND gs.game.status IN ('STARTING', 'IN_PROGRESS')")
  Optional<GameSession> findPlayerActiveGameSession(String session);
}
