package eitan.alon.app

import eitan.alon.clock.FixedClock
import eitan.alon.model.Command
import eitan.alon.model.User
import eitan.alon.repository.InMemoryMessageRepository
import eitan.alon.repository.InMemoryUserRepository
import eitan.alon.service.TimelineService
import eitan.alon.service.WallService
import java.time.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CommandHandlerTest {
    private val baseTime = Instant.parse("2024-01-01T12:00:00Z")
    private lateinit var messageRepository: InMemoryMessageRepository
    private lateinit var userRepository: InMemoryUserRepository
    private lateinit var clock: FixedClock
    private lateinit var handler: CommandHandler

    @BeforeTest
    fun setUp() {
        messageRepository = InMemoryMessageRepository()
        userRepository = InMemoryUserRepository()
        clock = FixedClock(baseTime)
        handler =
            CommandHandler(
                messageRepository = messageRepository,
                userRepository = userRepository,
                timelineService = TimelineService(messageRepository),
                wallService = WallService(messageRepository, userRepository),
                clock = clock,
            )
    }

    @Test
    fun `posting returns no output`() {
        assertNull(handler.handleCommand(Command.Post("Alice", "I love the weather today")))
    }

    @Test
    fun `following returns no output`() {
        userRepository.save(User("Charlie"))
        userRepository.save(User("Alice"))

        assertNull(handler.handleCommand(Command.Follow("Charlie", "Alice")))
    }

    @Test
    fun `reading shows messages newest first with relative time`() {
        handler.handleCommand(Command.Post("Bob", "Damn! We lost!"))
        clock.advanceTo(baseTime.plusSeconds(60))
        handler.handleCommand(Command.Post("Bob", "Good game though."))
        clock.advanceTo(baseTime.plusSeconds(120))

        val output = handler.handleCommand(Command.Read("Bob"))

        assertEquals(
            "Good game though. (1 minute ago)\nDamn! We lost! (2 minutes ago)",
            output,
        )
    }

    @Test
    fun `reading unknown user returns error message`() {
        assertEquals("User not found: 'UnknownUser'", handler.handleCommand(Command.Read("UnknownUser")))
    }

    @Test
    fun `wall of unknown user returns error message`() {
        assertEquals("User not found: 'UnknownUser'", handler.handleCommand(Command.Wall("UnknownUser")))
    }

    @Test
    fun `following with unknown follower returns error message`() {
        userRepository.save(User("Alice"))

        assertEquals("User not found: 'Unknown'", handler.handleCommand(Command.Follow("Unknown", "Alice")))
    }

    @Test
    fun `following with unknown followee returns error message`() {
        userRepository.save(User("Charlie"))

        assertEquals("User not found: 'Unknown'", handler.handleCommand(Command.Follow("Charlie", "Unknown")))
    }

    // --- formatTime ---

    private fun readAfterSeconds(seconds: Long): String? {
        handler.handleCommand(Command.Post("Alice", "msg"))
        clock.advanceTo(baseTime.plusSeconds(seconds))
        return handler.handleCommand(Command.Read("Alice"))
    }

    @Test
    fun `formatTime shows 0 seconds`() {
        assertEquals("msg (0 seconds ago)", readAfterSeconds(0))
    }

    @Test
    fun `formatTime shows 1 second`() {
        assertEquals("msg (1 second ago)", readAfterSeconds(1))
    }

    @Test
    fun `formatTime shows 30 seconds`() {
        assertEquals("msg (30 seconds ago)", readAfterSeconds(30))
    }

    @Test
    fun `formatTime shows 1 minute`() {
        assertEquals("msg (1 minute ago)", readAfterSeconds(60 + 5))
    }

    @Test
    fun `formatTime shows 2 minutes`() {
        assertEquals("msg (2 minutes ago)", readAfterSeconds(2 * 60 + 5))
    }

    @Test
    fun `formatTime shows 1 hour`() {
        assertEquals("msg (1 hour ago)", readAfterSeconds(60 * 60))
    }

    @Test
    fun `formatTime shows 2 hours`() {
        assertEquals("msg (2 hours ago)", readAfterSeconds(2 * 60 * 60))
    }

    @Test
    fun `formatTime shows 1 day`() {
        assertEquals("msg (1 day ago)", readAfterSeconds(24 * 60 * 60))
    }

    @Test
    fun `formatTime shows 2 days`() {
        assertEquals("msg (2 days ago)", readAfterSeconds(2 * 24 * 60 * 60))
    }

    // --- Wall ---

    @Test
    fun `wall shows own and followed messages newest first with author prefix`() {
        handler.handleCommand(Command.Post("Alice", "I love the weather today"))
        clock.advanceTo(baseTime.plusSeconds(2))
        handler.handleCommand(Command.Post("Charlie", "I'm in New York today!"))
        handler.handleCommand(Command.Follow("Charlie", "Alice"))
        clock.advanceTo(baseTime.plusSeconds(302))

        val output = handler.handleCommand(Command.Wall("Charlie"))

        assertEquals(
            "Charlie - I'm in New York today! (5 minutes ago)\nAlice - I love the weather today (5 minutes ago)",
            output,
        )
    }
}
