package com.matag.admin

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class MatagAdminApplication {
    fun main(args: Array<String>) {
        SpringApplication.run(MatagAdminApplication::class.java, *args)
    }
}