package com.matag.admin.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class MatagExceptionHandler {
    @ExceptionHandler(MatagException::class)
    fun handle(e: MatagException): ResponseEntity<ErrorResponse> {
        LOGGER.error("Error ${e.message}")
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(e.message.toString()))
    }

    @ExceptionHandler(InsufficientAuthenticationException::class)
    fun handle(e: InsufficientAuthenticationException): ResponseEntity<ErrorResponse> {
        LOGGER.error("Error {}", e.message, e)
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse(e.message.toString()))
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(MatagExceptionHandler::class.java)
    }
}
