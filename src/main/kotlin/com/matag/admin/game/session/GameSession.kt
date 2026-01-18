package com.matag.admin.game.session

import com.matag.admin.game.game.Game
import com.matag.admin.session.MatagSession
import com.matag.admin.user.MatagUser
import jakarta.persistence.*

@Entity
@Table(name = "game_session")
data class GameSession(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    var game: Game = Game(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    var session: MatagSession? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    var player: MatagUser? = null,

    var playerOptions: String? = null
)