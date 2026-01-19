package com.matag.admin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class MatagAdminApplication

fun main(args: Array<String>) {
    runApplication<MatagAdminApplication>(*args)
}
