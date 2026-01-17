package com.matag.admin

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling
import java.time.Clock

@Profile("!test")
@Configuration
@EnableScheduling
open class MatagAdminProdConfiguration {
    @Bean
    open fun clock() =
        Clock.systemDefaultZone()
}
