package com.matag.admin.user.verification

import com.matag.admin.user.MatagUser
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "matag_user_verification")
data class MatagUserVerification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    var matagUser: MatagUser? = null,
    var verificationCode: String? = null,
    var validUntil: LocalDateTime? = null,
    var attempts: Int? = null
)
