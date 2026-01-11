package com.matag.admin.auth.login

data class LoginResponse(
    var token: String = "",
    var profile: CurrentUserProfileDto = CurrentUserProfileDto(),
    var error: String = ""
)

data class CurrentUserProfileDto(
    var username: String = "",
    var type: String = ""
)
