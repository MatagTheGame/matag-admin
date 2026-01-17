package com.matag.admin.auth.register

import com.matag.admin.config.ConfigService
import com.matag.admin.email.MatagEmailSender
import lombok.SneakyThrows
import org.springframework.stereotype.Component

@Component
class RegisterEmailService(
    private val emailSender: MatagEmailSender,
    private val configService: ConfigService
) {
    @SneakyThrows
    fun sendRegistrationEmail(receiver: String, username: String, verificationCode: String) {
        emailSender.send(receiver, "Registration", createBody(username, verificationCode))
    }

    private fun createBody(username: String, verificationCode: String): String {
        val verificationLink = configService.matagExternalUrl + "/ui/auth/verify?username=$username&code=$verificationCode"

        return """
            <p>Hi $username,</p>
            <p>Welcome to <a href="${configService.matagExternalUrl} ">${configService.matagName}</a>.</p>
            <p>Please <a href="$verificationLink">click here</a> to verify your account.</p>"
            <p>The <em>${configService.matagName}</em> Team.</p>
        """
    }
}
