package com.matag.admin.user.profile

import com.matag.admin.auth.SecurityContextHolderHelper
import com.matag.admin.auth.login.CurrentUserProfileDto
import lombok.AllArgsConstructor
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@AllArgsConstructor
@RequestMapping("/profile")
open class CurrentUserProfileController {
    private val securityContextHolderHelper: SecurityContextHolderHelper? = null
    private val currentUserProfileService: CurrentUserProfileService? = null

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    open fun profile(): CurrentUserProfileDto =
        currentUserProfileService!!.getProfile(
            securityContextHolderHelper!!.getUser(),
            securityContextHolderHelper.getSession()
        )
}
