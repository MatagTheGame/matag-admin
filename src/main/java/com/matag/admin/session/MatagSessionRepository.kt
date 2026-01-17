package com.matag.admin.session;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MatagSessionRepository extends CrudRepository<MatagSession, Long> {
  Optional<MatagSession> findBySessionId(String sessionId);

  Optional<MatagSession> findByMatagUserId(Long id);

  @Modifying
  @Transactional
  @Query("DELETE FROM MatagSession WHERE validUntil < ?1")
  int deleteValidUntilBefore(LocalDateTime now);

  void deleteBySessionId(String sessionId);

  boolean existsBySessionId(String sessionId);
}
