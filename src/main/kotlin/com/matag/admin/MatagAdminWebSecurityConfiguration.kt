package com.matag.admin

import com.matag.admin.auth.logout.MatagLogoutSuccessHandler
import com.matag.admin.session.AuthSessionFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler
import org.springframework.security.web.context.SecurityContextPersistenceFilter
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
open class MatagAdminWebSecurityConfiguration {
    @Autowired
    private val matagLogoutSuccessHandler: MatagLogoutSuccessHandler? = null

    @Autowired
    private val authSessionFilter: AuthSessionFilter? = null

    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .addFilterAfter(authSessionFilter, SecurityContextPersistenceFilter::class.java)
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .logout {
                it
                    .logoutUrl("/auth/logout")
                    .addLogoutHandler(
                        HeaderWriterLogoutHandler(
                            ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.ALL)
                        )
                    )
                    .logoutSuccessHandler(matagLogoutSuccessHandler)
            }
            .authorizeHttpRequests{it.anyRequest().permitAll()}
            .build()

    @Bean
    open fun passwordEncoder(): PasswordEncoder =
        DelegatingPasswordEncoder(
            "argon2",
            mapOf("argon2" to Argon2PasswordEncoder(16, 32, 8, 1 shl 16, 4))
        )

    @Bean
    open fun authenticationManager(): AuthenticationManager =
        AuthenticationManager {
            throw AuthenticationServiceException("Cannot authenticate $it")
    }
}
