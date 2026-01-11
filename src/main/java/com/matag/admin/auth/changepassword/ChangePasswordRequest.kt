package com.matag.admin.auth.changepassword

data class ChangePasswordRequest(
    var oldPassword: String? = null,
    var newPassword: String? = null
)
