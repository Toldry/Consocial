package eitan.alon.clock

import java.time.Instant

class FixedClock(private var instant: Instant) : Clock {
    override fun now(): Instant = instant
    fun advanceTo(instant: Instant) {
        this.instant = instant
    }
}
