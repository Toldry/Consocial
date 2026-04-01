package eitan.alon.repository

import eitan.alon.model.Message
import eitan.alon.model.UserNotFoundException
import java.time.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class InMemoryMessageRepositoryTest {

    private val baseTime = Instant.parse("2024-01-01T12:00:00Z")
    private lateinit var repository: InMemoryMessageRepository

    @BeforeTest
    fun setUp() {
        repository = InMemoryMessageRepository()
    }

    @Test
    fun `findByAuthor throws UserNotFoundException for unknown user`() {
        assertFailsWith<UserNotFoundException> {
            repository.findByAuthor("Alice")
        }
    }

    @Test
    fun `findByAuthor returns saved messages for the correct author`() {
        repository.save(Message("Alice", "Hello", baseTime))

        val messages = repository.findByAuthor("Alice")

        assertEquals(1, messages.size)
        assertEquals("Hello", messages[0].content)
    }

    @Test
    fun `findByAuthor does not return messages from other authors`() {
        repository.save(Message("Alice", "Hello", baseTime))
        repository.save(Message("Bob", "Hi", baseTime.plusSeconds(10)))

        val messages = repository.findByAuthor("Alice")

        assertEquals(1, messages.size)
        assertEquals("Alice", messages[0].author)
    }

    @Test
    fun `findByAuthor returns multiple messages in insertion order`() {
        repository.save(Message("Alice", "First", baseTime))
        repository.save(Message("Alice", "Second", baseTime.plusSeconds(60)))
        repository.save(Message("Alice", "Third", baseTime.plusSeconds(120)))

        val messages = repository.findByAuthor("Alice")

        assertEquals(listOf("First", "Second", "Third"), messages.map { it.content })
    }

    @Test
    fun `findByAuthor returns message with correct author and postedAt`() {
        repository.save(Message("Alice", "Hello", baseTime))

        val message = repository.findByAuthor("Alice")[0]

        assertEquals("Alice", message.author)
        assertEquals(baseTime, message.postedAt)
    }
}
