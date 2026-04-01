package eitan.alon.clock

import java.time.Instant

/** Abstraction over the system clock, allowing deterministic time in tests. */
interface Clock {
    /** Returns the current instant. */
    fun now(): Instant
}

/** Production implementation of [Clock] */
class SystemClock : Clock {
    override fun now(): Instant = TODO()
}
