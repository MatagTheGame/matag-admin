package com.matag.admin.cleanup

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class MatagCleanup {
    private val matagSessionsCleanup: MatagSessionsCleanup? = null
    private val matagGameCleanup: MatagGameCleanup? = null

    @Scheduled(fixedRate = 6 * 60 * 60 * 1000, initialDelay = 10 * 60 * 1000)
    fun cleanup() {
        LOGGER.info("cleanup triggered.")

        matagSessionsCleanup!!.cleanup()
        matagGameCleanup!!.cleanup()
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(MatagCleanup::class.java)
    }
}
