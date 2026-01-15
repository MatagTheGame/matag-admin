package com.matag.admin.auth.validators

import org.springframework.stereotype.Component
import java.util.regex.Pattern

@Component
open class UsernameValidator : Validator<String> {
    override fun validate(field: String?) {
        if (field == null || !pattern.matcher(field).matches()) {
            throw ValidationException(USERNAME_IS_INVALID)
        }
    }

    companion object {
        private const val REGEX = "^[A-Za-z0-9 +\\-*=_.@&]{3,25}$"
        private val pattern: Pattern = Pattern.compile(REGEX)
        private const val USERNAME_IS_INVALID =
            "Username needs to be between 4 and 25 characters and can contains only letters  number and one of the following characters: [+ - * = _ . @ &]."
    }
}
