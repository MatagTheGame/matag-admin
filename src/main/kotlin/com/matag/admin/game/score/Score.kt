package com.matag.admin.game.score

import com.matag.admin.game.game.GameType
import com.matag.admin.user.MatagUser
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "score")
data class Score(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    var matagUser: MatagUser? = null,

    @Enumerated(EnumType.STRING)
    var type: GameType? = null,
    var elo: Int? = null,
    var wins: Int? = null,
    var draws: Int? = null,
    var losses: Int? = null,
    var lastGamePlayedAt: LocalDateTime? = null
)
