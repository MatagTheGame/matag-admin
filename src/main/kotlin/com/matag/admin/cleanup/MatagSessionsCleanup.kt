package com.matag.admin.cleanup

import com.matag.admin.session.MatagSessionRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.LocalDateTime

@Component
open class MatagSessionsCleanup(
    private val matagSessionRepository: MatagSessionRepository,
    private val clock: Clock
) {

    @Transactional
    open fun cleanup() {
        val deletedRows = matagSessionRepository.deleteValidUntilBefore(LocalDateTime.now(clock))
        LOGGER.info("MatagSessionsCleanup deleted $deletedRows rows.")
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(MatagSessionsCleanup::class.java)
    }
}
