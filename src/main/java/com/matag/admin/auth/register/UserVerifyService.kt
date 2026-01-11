package com.matag.admin.auth.register

import com.matag.admin.auth.codes.RandomCodeService
import com.matag.admin.user.MatagUser
import com.matag.admin.user.verification.MatagUserVerification
import com.matag.admin.user.verification.MatagUserVerificationRepository
import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.LocalDateTime

@Component
open class UserVerifyService(
    private val randomCodeService: RandomCodeService,
    private val userVerificationRepository: MatagUserVerificationRepository,
    private val clock: Clock
) {

    open fun createVerification(user: MatagUser?): MatagUserVerification {
        val verificationCode = randomCodeService.generatesRandomCode()
        val matagUserVerification = MatagUserVerification(
            verificationCode = verificationCode,
            matagUser = user,
            validUntil = LocalDateTime.now(clock).plusDays(1),
            attempts = 0
        )

        userVerificationRepository.save(matagUserVerification)

        return matagUserVerification
    }

    open fun verify(user: MatagUser, code: String?) {
        val matagUserVerification = user.matagUserVerification
        if (matagUserVerification!!.attempts!! >= MAX_ATTEMPTS) {
            increaseAttempts(matagUserVerification)
            throw RuntimeException("User " + user.username + " too many attempts. times: " + matagUserVerification.attempts)
        }

        if (LocalDateTime.now(clock).isAfter(matagUserVerification.validUntil)) {
            increaseAttempts(matagUserVerification)
            throw RuntimeException("User " + user.username + " validation code expired on: " + matagUserVerification.validUntil + " . times: " + matagUserVerification.attempts)
        }

        if (matagUserVerification.verificationCode != code) {
            increaseAttempts(matagUserVerification)
            throw RuntimeException("User " + user.username + " attempting a wrong code: " + code + " times: " + matagUserVerification.attempts)
        }

        matagUserVerification.verificationCode = null
        matagUserVerification.validUntil = null
        matagUserVerification.attempts = 0
        userVerificationRepository.save(matagUserVerification)
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    open fun increaseAttempts(matagUserVerification: MatagUserVerification) {
        matagUserVerification.attempts = matagUserVerification.attempts!! + 1
        userVerificationRepository.save(matagUserVerification)
    }

    companion object {
        private const val MAX_ATTEMPTS = 3
    }
}
