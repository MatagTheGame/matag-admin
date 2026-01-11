package com.matag.admin.auth.register

data class RegisterRequest(
    var email: String = "",
    var username: String = "",
    var password: String = ""
)
