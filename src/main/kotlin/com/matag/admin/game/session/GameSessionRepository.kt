package com.matag.admin.game.session

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GameSessionRepository : CrudRepository<GameSession, Long> {
    @Query("FROM GameSession gs WHERE gs.session.sessionId = ?1 AND gs.game.status IN ('STARTING', 'IN_PROGRESS')")
    fun findPlayerActiveGameSession(session: String): GameSession?
}
