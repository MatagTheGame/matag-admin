package com.matag.admin.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ConfigService(
    @param:Value("\${matag.name}")
    val matagName: String,

    @param:Value("\${matag.game.path}")
    val matagGamePath: String,

    @param:Value("\${matag.admin.path}")
    val matagAdminPath: String,

    @param:Value("\${matag.admin.externalUrl}")
    val matagExternalUrl: String,

    @param:Value("\${matag.admin.password}")
    val matagAdminPassword: String,

    @param:Value("\${matag.email.username}")
    val matagSupportEmail: String
) {
    fun getConfig() = mapOf(
            "matagName" to matagName,
            "matagAdminUrl" to matagAdminPath,
            "matagGameUrl" to matagGamePath,
            "matagSupportEmail" to matagSupportEmail
        )
}
