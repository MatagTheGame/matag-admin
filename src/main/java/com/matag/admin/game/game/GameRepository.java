package com.matag.admin.game.game;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {
  List<Game> findByTypeAndStatus(GameType type, GameStatusType status);

  @Query("FROM Game WHERE status in (?1) AND createdAt < ?2")
  List<Game> findOldGameByStatus(List<GameStatusType> status, LocalDateTime now);

  @Query("FROM Game g JOIN FETCH GameSession gs on g.id = gs.game.id WHERE gs.player.id = ?1 AND g.status = ?2")
  List<Game> findByPlayerIdAndStatus(Long playerId, GameStatusType status);
}
