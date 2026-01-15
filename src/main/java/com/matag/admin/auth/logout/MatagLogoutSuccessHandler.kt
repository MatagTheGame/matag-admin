package com.matag.admin.auth.logout

import com.matag.admin.session.AuthSessionFilter
import com.matag.admin.session.MatagSessionRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.AllArgsConstructor
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import java.io.IOException

@Component
open class MatagLogoutSuccessHandler(private val matagSessionRepository: MatagSessionRepository) : LogoutSuccessHandler {

    @Transactional
    @Throws(IOException::class)
    override fun onLogoutSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication?
    ) {
        val sessionId = request.getHeader(AuthSessionFilter.SESSION_NAME)
        if (StringUtils.hasText(sessionId)) {
            matagSessionRepository.deleteBySessionId(sessionId)
        }

        response.setStatus(HttpServletResponse.SC_OK)
        response.getWriter().flush()
    }
}
