package com.matag.admin.auth.changepassword

import com.matag.admin.auth.SecurityContextHolderHelper
import com.matag.admin.auth.login.LoginController
import com.matag.admin.auth.validators.PasswordValidator
import com.matag.admin.auth.validators.ValidationException
import com.matag.admin.exception.MatagException
import com.matag.admin.user.MatagUser
import com.matag.admin.user.MatagUserRepository
import lombok.AllArgsConstructor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Clock
import java.time.LocalDateTime

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
open class ChangePasswordController(
    @param:Autowired private val securityContextHolderHelper: SecurityContextHolderHelper,
    @param:Autowired private val passwordEncoder: PasswordEncoder,
    @param:Autowired private val passwordValidator: PasswordValidator,
    @param:Autowired private val clock: Clock,
    @param:Autowired private val userRepository: MatagUserRepository
) {


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/change-password")
    open fun changePassword(@RequestBody request: ChangePasswordRequest): ChangePasswordResponse {
        val user = securityContextHolderHelper.user

        validate(request, user)

        val newPasswordEncoded = passwordEncoder.encode(request.newPassword)
        user.password = newPasswordEncoded
        user.updatedAt = LocalDateTime.now(clock)
        userRepository.save(user)
        LOGGER.info("Password successfully changed for user " + user.username)

        return ChangePasswordResponse("Password changed.")
    }

    private fun validate(@RequestBody request: ChangePasswordRequest, user: MatagUser) {
        try {
            passwordValidator.validate(request.newPassword)
        } catch (e: ValidationException) {
            throw MatagException("The new password you chose is invalid: " + e.message)
        }

        if (!passwordEncoder.matches(request.oldPassword, user.getPassword())) {
            throw MatagException("Your password wasn't matched.")
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(LoginController::class.java)
    }
}
