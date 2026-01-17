package com.matag.admin.user

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface MatagUserRepository : CrudRepository<MatagUser, Long> {
    fun findByEmailAddress(username: String): MatagUser?

    fun findByUsername(username: String): MatagUser?

    @Query("SELECT COUNT(id) FROM MatagUser WHERE status = ?1")
    fun countUsersByStatus(userStatus: MatagUserStatus): Long

    @Query("SELECT u.username FROM MatagUser u JOIN MatagSession ms on u.id = ms.matagUser.id WHERE ms.validUntil > ?1")
    fun retrieveOnlineUsers(now: LocalDateTime): List<String>
}
