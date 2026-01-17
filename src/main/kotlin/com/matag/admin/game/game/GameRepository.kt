package com.matag.admin.game.game

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface GameRepository : CrudRepository<Game, Long> {
    fun findByTypeAndStatus(type: GameType, status: GameStatus): List<Game>

    @Query("FROM Game g JOIN FETCH GameSession gs on g.id = gs.game.id WHERE gs.player.id = ?1 AND g.status = ?2")
    fun findByPlayerIdAndStatus(playerId: Long, status: GameStatus): List<Game>

    @Query("FROM Game g WHERE g.status IN (?1) AND g.createdAt < ?2")
    fun findOldGameByStatus(status: List<GameStatus>, now: LocalDateTime): List<Game>

    @Query("SELECT g.id FROM Game g JOIN GameSession gs on g.id = gs.game.id WHERE gs.player.id = 1 GROUP BY g.id HAVING COUNT(gs.player.id) > 1")
    fun findFinishedGuestGames(minusDays: LocalDateTime): List<Long>
}
