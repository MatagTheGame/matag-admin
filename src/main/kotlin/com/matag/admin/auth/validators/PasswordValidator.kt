package com.matag.admin.auth.validators

import org.springframework.stereotype.Component

@Component
open class PasswordValidator : Validator<String> {
    override fun validate(field: String?) {
        if (field == null || field.length < 4) {
            throw ValidationException(PASSWORD_IS_INVALID)
        }
    }

    companion object {
        private const val PASSWORD_IS_INVALID = "Password is invalid (should be at least 4 characters)."
    }
}
