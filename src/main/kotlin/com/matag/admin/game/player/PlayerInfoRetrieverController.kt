package com.matag.admin.game.player

import com.matag.admin.auth.SecurityContextHolderHelper
import com.matag.admin.session.MatagSession
import com.matag.admin.user.MatagUserType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/player/info")
open class PlayerInfoRetrieverController(
    private val securityContextHolderHelper: SecurityContextHolderHelper
) {
    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    @GetMapping
    open fun deckInfo(): PlayerInfo {
        val session = securityContextHolderHelper.getSession()
        return PlayerInfo(getUsername(session))
    }

    private fun getUsername(session: MatagSession): String? {
        return session.matagUser?.let {
            if (it.type == MatagUserType.GUEST) {
                it.username + "-" + session.id
            } else {
                it.username
            }
        }
    }
}
