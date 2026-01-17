package com.matag.admin.auth.validators

import org.springframework.stereotype.Component
import java.util.regex.Pattern

@Component
open class EmailValidator : Validator<String> {
    override fun validate(field: String?) {
        if (field == null || !pattern.matcher(field).matches() || field.length > 100) {
            throw ValidationException(EMAIL_IS_INVALID)
        }
    }

    companion object {
        private const val REGEX = "^[A-Za-z0-9+_.-]+@(.+)$"
        private val pattern: Pattern = Pattern.compile(REGEX)
        private const val EMAIL_IS_INVALID = "Email is invalid."
    }
}
