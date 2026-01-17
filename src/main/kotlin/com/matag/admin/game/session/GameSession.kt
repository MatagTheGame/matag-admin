package com.matag.admin.game.session

import com.matag.admin.game.game.Game
import com.matag.admin.session.MatagSession
import com.matag.admin.user.MatagUser
import jakarta.persistence.*

@Entity
@Table(name = "game_session")
data class GameSession(
    @JvmField
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @JvmField
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    var game: Game = Game(),

    @JvmField
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    var session: MatagSession? = null,

    @JvmField
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    var player: MatagUser? = null,

    @JvmField
    var playerOptions: String? = null
)