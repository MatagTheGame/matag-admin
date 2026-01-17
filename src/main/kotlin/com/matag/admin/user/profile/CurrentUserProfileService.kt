package com.matag.admin.user.profile

import com.matag.admin.auth.login.CurrentUserProfileDto
import com.matag.admin.session.MatagSession
import com.matag.admin.user.MatagUser
import com.matag.admin.user.MatagUserType
import org.springframework.stereotype.Component

@Component
class CurrentUserProfileService {
    fun getProfile(matagUser: MatagUser, session: MatagSession): CurrentUserProfileDto {
        return CurrentUserProfileDto(
            username = getUsername(matagUser, session),
            type = matagUser.type.toString()
        )
    }

    private fun getUsername(matagUser: MatagUser, session: MatagSession): String {
        if (matagUser.type == MatagUserType.GUEST) {
            return matagUser.username + "-" + session.id
        } else {
            return matagUser.username!!
        }
    }
}
