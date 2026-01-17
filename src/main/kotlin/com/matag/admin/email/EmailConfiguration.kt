package com.matag.admin.email

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.mail.javamail.JavaMailSenderImpl

@Profile("!test")
@Configuration
open class EmailConfiguration {
    @Bean
    open fun javaMailSender(
        @Value("\${matag.email.username}") matagEmailUsername: String,
        @Value("\${matag.email.password}") matagEmailPassword: String
    ) = JavaMailSenderImpl().apply {
            host = "smtp.gmail.com"
            port = 587

            username = matagEmailUsername
            password = matagEmailPassword

            javaMailProperties["mail.transport.protocol"] = "smtp"
            javaMailProperties["mail.smtp.auth"] = "true"
            javaMailProperties["mail.smtp.starttls.enable"] = "true"
            javaMailProperties["mail.debug"] = "false"
        }
}
