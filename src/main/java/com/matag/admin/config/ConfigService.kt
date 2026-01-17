package com.matag.admin.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Map

@Component
class ConfigService(
    @Value("\${matag.name}")
    val matagName: String,

    @Value("\${matag.game.path}")
    val matagGamePath: String,

    @Value("\${matag.admin.path}")
    val matagAdminPath: String,

    @Value("\${matag.admin.externalUrl}")
    val matagExternalUrl: String,

    @Value("\${matag.admin.password}")
    val matagAdminPassword: String,

    @Value("\${matag.email.username}")
    val matagSupportEmail: String
) {
    fun getConfig() = Map.of(
            "matagName", matagName,
            "matagAdminUrl", matagAdminPath,
            "matagGameUrl", matagGamePath,
            "matagSupportEmail", matagSupportEmail
        )
}
