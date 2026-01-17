package com.matag.admin.config

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/config")
open class ConfigController(
    private val configService: ConfigService
) {
    @PreAuthorize("permitAll()")
    @GetMapping
    open fun config() =
        configService.getConfig()
}
