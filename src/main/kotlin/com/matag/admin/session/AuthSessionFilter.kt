package com.matag.admin.session

import com.matag.admin.config.ConfigService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.security.web.firewall.FirewalledRequest
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import java.time.Clock
import java.time.LocalDateTime

@Component
class AuthSessionFilter(
    private val configService: ConfigService,
    private val matagSessionRepository: MatagSessionRepository,
    private val clock: Clock
) : GenericFilterBean() {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, filterChain: FilterChain) {
        if (request is FirewalledRequest) {
            applySecurity(request)
        }
        filterChain.doFilter(request, response)
    }

    private fun applySecurity(request: FirewalledRequest) {
        val adminPassword = request.getHeader(ADMIN_NAME)
        val userSessionId = request.getHeader(SESSION_NAME)

        if (StringUtils.hasText(adminPassword)) {
            adminAuthentication(adminPassword)
            logger.info("Received request for admin: ${request.method} ${request.requestURI}")
        } else if (StringUtils.hasText(userSessionId)) {
            val user = userAuthentication(userSessionId)
            logger.info("Received request for user[session=$userSessionId, user=$user]: ${request.method} ${request.requestURI}")
        }
    }

    private fun adminAuthentication(adminPassword: String) {
        if (configService.matagAdminPassword == adminPassword) {
            val authorities = listOf(SimpleGrantedAuthority("ROLE_ADMIN"))
            val authentication = PreAuthenticatedAuthenticationToken("admin", configService.matagAdminPassword, authorities)
            SecurityContextHolder.getContext().authentication = authentication
        }
    }

    private fun userAuthentication(sessionId: String): String? {
        val matagSession = matagSessionRepository.findBySessionId(sessionId)

        if (matagSession != null) {
            if (LocalDateTime.now(clock).isBefore(matagSession.validUntil)) {
                val aPrincipal = matagSession.matagUser!!
                val authorities = listOf(SimpleGrantedAuthority("ROLE_" + aPrincipal.type.toString()))
                val authentication = PreAuthenticatedAuthenticationToken(aPrincipal, matagSession, authorities)
                SecurityContextHolder.getContext().authentication = authentication

                if (LocalDateTime.now(clock).plusSeconds((SESSION_DURATION_TIME / 2).toLong()).isAfter(matagSession.validUntil)) {
                    matagSession.validUntil = LocalDateTime.now(clock).plusSeconds(SESSION_DURATION_TIME.toLong())
                    matagSessionRepository.save(matagSession)
                }
            }
        }

        return matagSession?.matagUser?.username
    }

    companion object {
        const val SESSION_NAME: String = "session"
        const val ADMIN_NAME: String = "admin"
        val SESSION_DURATION_TIME: Int = 60 * 60
    }
}
