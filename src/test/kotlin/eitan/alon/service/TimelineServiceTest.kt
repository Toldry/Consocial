package eitan.alon.service

import eitan.alon.model.Message
import eitan.alon.model.UserNotFoundException
import eitan.alon.repository.InMemoryMessageRepository
import java.time.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TimelineServiceTest {
    private lateinit var repository: InMemoryMessageRepository
    private lateinit var service: TimelineService
    private val baseTime = Instant.parse("2024-01-01T12:00:00Z")

    @BeforeTest
    fun setUp() {
        repository = InMemoryMessageRepository()
        service = TimelineService(repository)
    }

    @Test
    fun `returns messages for the correct user`() {
        repository.save(Message("Alice", "Hello", baseTime))
        repository.save(Message("Bob", "Hi", baseTime))

        val timeline = service.getTimeline("Alice")

        assertEquals(1, timeline.size)
        assertEquals("Alice", timeline[0].author)
    }

    @Test
    fun `returns messages in reverse chronological order`() {
        repository.save(Message("Alice", "First", baseTime))
        repository.save(Message("Alice", "Second", baseTime.plusSeconds(60)))
        repository.save(Message("Alice", "Third", baseTime.plusSeconds(120)))

        val timeline = service.getTimeline("Alice")

        assertEquals(listOf("Third", "Second", "First"), timeline.map { it.content })
    }

    @Test
    fun `throws UserNotFoundException for unknown user`() {
        assertFailsWith<UserNotFoundException> {
            service.getTimeline("NonExistentUser")
        }
    }
}
