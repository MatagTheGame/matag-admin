package com.matag.admin.auth.login

data class LoginRequest(
    var emailOrUsername: String = "",
    var password: String = ""
)
