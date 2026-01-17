package application

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset

class MockClock : Clock() {
    private var currentTime: Instant? = null

    override fun getZone(): ZoneId? {
        return ZoneOffset.UTC
    }

    override fun withZone(zone: ZoneId?): Clock {
        return this
    }

    override fun instant(): Instant? {
        return currentTime
    }

    fun setCurrentTime(currentTime: Instant?) {
        this.currentTime = currentTime
    }
}
