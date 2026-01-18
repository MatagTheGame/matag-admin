package com.matag.admin.auth.register

import com.matag.admin.auth.validators.EmailValidator
import com.matag.admin.auth.validators.PasswordValidator
import com.matag.admin.auth.validators.UsernameValidator
import com.matag.admin.auth.validators.ValidationException
import com.matag.admin.config.ConfigService
import com.matag.admin.exception.MatagException
import com.matag.admin.user.MatagUserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
open class RegisterController(
    private val configService: ConfigService,
    private val userRepository: MatagUserRepository,
    private val emailValidator: EmailValidator,
    private val usernameValidator: UsernameValidator,
    private val registerService: RegisterService,
    private val passwordValidator: PasswordValidator
) {
    @PreAuthorize("permitAll()")
    @PostMapping("/register")
    open fun register(@RequestBody request: RegisterRequest): RegisterResponse? {
        LOGGER.info("User {} registering with username[{}].", request.email, request.username)

        validate(request)

        registerService.register(request.email, request.username, request.password)
        LOGGER.info("Registration successful.")

        return RegisterResponse(REGISTERED_VERIFY_EMAIL)
    }

    @GetMapping("/verify")
    open fun verify(@RequestParam("username") username: String, @RequestParam("code") code: String): VerifyResponse {
        LOGGER.info("Verifying {} with code {}", username, code)
        try {
            registerService.activate(username, code)
            return VerifyResponse(ACCOUNT_VERIFICATION_CORRECT)
        } catch (e: Exception) {
            LOGGER.warn(e.message)
            throw MatagException(
                ACCOUNT_VERIFICATION_ERROR.replace(
                    "SUPPORT_MAIL",
                    configService.matagSupportEmail
                )
            )
        }
    }

    private fun validate(@RequestBody request: RegisterRequest) {
        emailValidator.validate(request.email)
        usernameValidator.validate(request.username)
        passwordValidator.validate(request.password)

        if (userRepository.findByEmailAddress(request.email) != null) {
            throw ValidationException(EMAIL_ALREADY_REGISTERED)
        }

        if (userRepository.findByUsername(request.username) != null) {
            throw ValidationException(USERNAME_ALREADY_REGISTERED)
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(RegisterController::class.java)

        private const val EMAIL_ALREADY_REGISTERED = "This email is already registered."
        private const val USERNAME_ALREADY_REGISTERED = "This username is already registered (please choose a new one)."
        private const val REGISTERED_VERIFY_EMAIL =
            "Registration Successful. Please check your email for a verification code."
        private const val ACCOUNT_VERIFICATION_CORRECT =
            "Your account has been correctly verified. Now you can proceed with logging in."
        private const val ACCOUNT_VERIFICATION_ERROR =
            "Your account could not be verified. Please send a message to SUPPORT_MAIL."
    }
}
