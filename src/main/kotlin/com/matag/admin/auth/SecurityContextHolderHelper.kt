package com.matag.admin.auth

import com.matag.admin.session.MatagSession
import com.matag.admin.user.MatagUser
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
open class SecurityContextHolderHelper {
    fun getUser() =
        SecurityContextHolder.getContext().authentication?.principal as MatagUser

    fun getSession() =
        SecurityContextHolder.getContext().authentication?.credentials as MatagSession
}
