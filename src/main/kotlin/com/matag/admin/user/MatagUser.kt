package com.matag.admin.user

import com.matag.admin.user.verification.MatagUserVerification
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "matag_user")
class MatagUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var username: String? = null,
    var password: String? = null,
    var emailAddress: String? = null,

    @Enumerated(EnumType.STRING)
    var status: MatagUserStatus? = null,

    @Enumerated(EnumType.STRING)
    var type: MatagUserType? = null,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,

    @OneToOne(mappedBy = "matagUser", cascade = [CascadeType.REMOVE])
    var matagUserVerification: MatagUserVerification? = null
) {
    override fun toString(): String {
        return "MatagUser(id=$id, username=$username, emailAddress=$emailAddress, status=$status, type=$type)"
    }
}

enum class MatagUserType {
    ADMIN, USER, GUEST
}

enum class MatagUserStatus {
    ACTIVE, INACTIVE, VERIFYING
}
