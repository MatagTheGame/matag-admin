package com.matag.admin.email

import com.matag.admin.config.ConfigService
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component

@Component
class MatagEmailSender(
    private val emailSender: JavaMailSender,
    private val configService: ConfigService,
) {
    fun send(receiver: String, subject: String?, body: String) {
        val mimeMessage = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, "utf-8")
        helper.setTo(receiver)
        helper.setSubject(configService.matagName + " - " + subject)
        helper.setText(body, true)
        emailSender.send(mimeMessage)
    }
}
