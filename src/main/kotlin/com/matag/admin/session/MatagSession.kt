package com.matag.admin.session

import com.matag.admin.user.MatagUser
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "matag_session")
data class MatagSession(
    @JvmField
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @JvmField
    var sessionId: String? = null,

    @JvmField
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    var matagUser: MatagUser? = null,
    var createdAt: LocalDateTime? = null,
    var validUntil: LocalDateTime? = null
)
