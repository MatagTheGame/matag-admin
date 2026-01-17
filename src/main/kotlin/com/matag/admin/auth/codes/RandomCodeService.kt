package com.matag.admin.auth.codes

import org.springframework.stereotype.Component
import java.util.*
import java.util.function.BiConsumer
import java.util.function.IntPredicate
import java.util.function.ObjIntConsumer
import java.util.function.Supplier

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
