package com.matag.admin.session

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Repository
interface MatagSessionRepository : CrudRepository<MatagSession, Long> {
    fun findBySessionId(sessionId: String): MatagSession?

    fun findByMatagUserId(id: Long): MatagSession?

    @Modifying
    @Transactional
    @Query("DELETE FROM MatagSession WHERE validUntil < ?1")
    fun deleteValidUntilBefore(now: LocalDateTime): Int

    fun deleteBySessionId(sessionId: String)

    fun existsBySessionId(sessionId: String): Boolean
}
