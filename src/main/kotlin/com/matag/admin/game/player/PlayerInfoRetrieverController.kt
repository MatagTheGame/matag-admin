package com.matag.admin.game.player

import com.matag.admin.auth.SecurityContextHolderHelper
import com.matag.admin.session.MatagSession
import com.matag.admin.user.MatagUserType
import lombok.AllArgsConstructor
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@AllArgsConstructor
@RestController
@RequestMapping("/player/info")
open class PlayerInfoRetrieverController {
    private val securityContextHolderHelper: SecurityContextHolderHelper? = null

    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    @GetMapping
    open fun deckInfo(): PlayerInfo {
        val session = securityContextHolderHelper!!.getSession()
        return PlayerInfo(getUsername(session))
    }

    private fun getUsername(session: MatagSession): String? {
        val matagUser = session.matagUser
        if (matagUser!!.type == MatagUserType.GUEST) {
            return matagUser.username + "-" + session.id
        } else {
            return matagUser.username
        }
    }
}
