package com.matag.admin.auth.register

import com.matag.admin.game.score.ScoreService
import com.matag.admin.user.MatagUser
import com.matag.admin.user.MatagUserRepository
import com.matag.admin.user.MatagUserStatus
import com.matag.admin.user.MatagUserType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.LocalDateTime

@Component
open class RegisterService(
  private val passwordEncoder: PasswordEncoder,
  private val clock: Clock,
  private val userRepository: MatagUserRepository,
  private val scoreService: ScoreService,
  private val registerEmailService: RegisterEmailService,
  private val userVerifyService: UserVerifyService
) {
    @Transactional
    open fun register(email: String, username: String, password: String?) {
        val user = MatagUser(
            username = username,
            password = passwordEncoder.encode(password),
            emailAddress = email,
            createdAt = LocalDateTime.now(clock),
            updatedAt = LocalDateTime.now(clock),
            status = MatagUserStatus.VERIFYING,
            type = MatagUserType.USER
        )
        userRepository.save(user)
        scoreService.createStartingScore(user)

        val verification = userVerifyService.createVerification(user)

        registerEmailService.sendRegistrationEmail(email, username, verification.verificationCode!!)
    }

    open fun activate(username: String, code: String) {
        val user = userRepository.findByUsername(username)
        if (user == null) {
            throw RuntimeException("User $username not found.")
        }

        when (user.status) {
            MatagUserStatus.INACTIVE -> throw RuntimeException("User $username is inactive and cannot be activated.")
            MatagUserStatus.ACTIVE -> throw RuntimeException("User $username is already active.")

            MatagUserStatus.VERIFYING -> {
                userVerifyService.verify(user, code)

              user.status = MatagUserStatus.ACTIVE
              user.updatedAt = LocalDateTime.now(clock)
              userRepository.save(user)
            }

            null -> throw RuntimeException("User $username has null status.")
        }
    }
}
