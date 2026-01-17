package com.matag.admin.game.game;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {
  List<Game> findByTypeAndStatus(GameType type, GameStatusType status);

  @Query("FROM Game g JOIN FETCH GameSession gs on g.id = gs.game.id WHERE gs.player.id = ?1 AND g.status = ?2")
  List<Game> findByPlayerIdAndStatus(Long playerId, GameStatusType status);

  @Query("FROM Game g WHERE g.status IN (?1) AND g.createdAt < ?2")
  List<Game> findOldGameByStatus(List<GameStatusType> status, LocalDateTime now);

  @Query("SELECT g.id FROM Game g JOIN GameSession gs on g.id = gs.game.id WHERE gs.player.id = 1 GROUP BY g.id HAVING COUNT(gs.player.id) > 1")
  List<Long> findFinishedGuestGames(LocalDateTime minusDays);
}
