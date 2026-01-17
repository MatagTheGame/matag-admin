package com.matag.admin.game.score

import com.matag.admin.user.MatagUser
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ScoreRepository : CrudRepository<Score, Long> {
    @Query("FROM Score s ORDER BY s.elo DESC, s.matagUser.username ASC")
    override fun findAll(): List<Score>

    fun findByMatagUser(user: MatagUser): Score?

    @Query("FROM Score s WHERE s.matagUser.username = ?1")
    fun findByUsername(username: String): Score?
}
