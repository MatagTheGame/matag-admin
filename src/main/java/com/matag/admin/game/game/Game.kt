package com.matag.admin.game.game

import com.matag.admin.game.session.GameSession
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "game")
data class Game(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Enumerated(EnumType.STRING)
    var type: GameType? = null,

    @Enumerated(EnumType.STRING)
    var status: GameStatus? = null,

    @Enumerated(EnumType.STRING)
    var result: GameResultType? = null,

    var finishedAt: LocalDateTime? = null,

    @OneToMany(mappedBy = "game", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    var gameSessions: MutableList<GameSession> = mutableListOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Game) return false
        return id != null && id == other.id
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0

    override fun toString(): String = "Game(id=$id, type=$type, status=$status)"
}