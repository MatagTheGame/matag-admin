package com.matag.admin.game.game;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {
  List<Game> findByTypeAndStatus(GameType type, GameStatusType status);

  @Query("FROM Game WHERE status = ?1 AND createdAt < ?2")
  List<Game> findOldGameByStatus(GameStatusType status, LocalDateTime now);
}
