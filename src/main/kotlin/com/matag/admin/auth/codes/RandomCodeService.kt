package com.matag.admin.auth.codes

import org.springframework.stereotype.Component

@Component
open class RandomCodeService {
    open fun generatesRandomCode(): String {
        val length = 10
        val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return buildString(length) {
            repeat(length) {
                append(charPool.random())
            }
        }
    }
}
