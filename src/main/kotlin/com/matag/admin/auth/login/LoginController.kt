package com.matag.admin.auth.login

import com.matag.admin.auth.validators.EmailValidator
import com.matag.admin.auth.validators.PasswordValidator
import com.matag.admin.auth.validators.ValidationException
import com.matag.admin.session.AuthSessionFilter
import com.matag.admin.session.MatagSession
import com.matag.admin.session.MatagSessionRepository
import com.matag.admin.user.MatagUser
import com.matag.admin.user.MatagUserRepository
import com.matag.admin.user.MatagUserStatus
import com.matag.admin.user.MatagUserType
import com.matag.admin.user.profile.CurrentUserProfileService
import lombok.AllArgsConstructor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Clock
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
open class LoginController(
    private val userRepository: MatagUserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val matagSessionRepository: MatagSessionRepository,
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator,
    private val currentUserProfileService: CurrentUserProfileService,
    private val clock: Clock
) {

    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    open fun login(@RequestBody loginRequest: LoginRequest): LoginResponse? {
        LOGGER.info("User ${loginRequest.emailOrUsername} logging in.")

        val user = validateLogin(loginRequest)

        if (user.type != MatagUserType.GUEST) {
            val existingSession = matagSessionRepository.findByMatagUserId(user.id!!)
            if (existingSession != null) {
                LOGGER.info("User was already logged in... restored its session.")
                existingSession.validUntil = LocalDateTime.now(clock).plusSeconds(AuthSessionFilter.SESSION_DURATION_TIME.toLong())
                matagSessionRepository.save(existingSession)
                return buildResponse(user, existingSession)
            }
        }

        val session = MatagSession(
            sessionId = UUID.randomUUID().toString(),
            matagUser = user,
            createdAt = LocalDateTime.now(clock),
            validUntil = LocalDateTime.now(clock).plusSeconds(AuthSessionFilter.SESSION_DURATION_TIME.toLong())
        )
        matagSessionRepository.save(session)

        LOGGER.info("Login successful.")
        return buildResponse(user, session)
    }

    private fun validateLogin(@RequestBody loginRequest: LoginRequest): MatagUser {
        passwordValidator.validate(loginRequest.password)

        val email = isEmailLogin(loginRequest)
        val user = getUsername(loginRequest.emailOrUsername, email)

        if (user == null) {
            throw InsufficientAuthenticationException(EMAIL_USERNAME_OR_PASSWORD_ARE_INCORRECT)
        }

        if (!passwordEncoder.matches(loginRequest.password, user.password)) {
            throw InsufficientAuthenticationException(EMAIL_USERNAME_OR_PASSWORD_ARE_INCORRECT)
        }

        if (user.status != MatagUserStatus.ACTIVE) {
            throw InsufficientAuthenticationException(ACCOUNT_IS_NOT_ACTIVE)
        }
        return user
    }

    private fun isEmailLogin(@RequestBody loginRequest: LoginRequest): Boolean {
        try {
            emailValidator.validate(loginRequest.emailOrUsername)
            return true
        } catch (_: ValidationException) {
            return false
        }
    }

    private fun getUsername(emailOrUsername: String, email: Boolean) =
        if (email) {
            userRepository.findByEmailAddress(emailOrUsername)
        } else {
            userRepository.findByUsername(emailOrUsername)
        }

    private fun buildResponse(user: MatagUser?, session: MatagSession): LoginResponse {
        return LoginResponse(
            token = session.sessionId!!,
            profile = currentUserProfileService.getProfile(user!!, session)
        )
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(LoginController::class.java)

        const val EMAIL_USERNAME_OR_PASSWORD_ARE_INCORRECT: String = "Email/Username or password are not correct."
        const val ACCOUNT_IS_NOT_ACTIVE: String = "Account is not active."
    }
}
