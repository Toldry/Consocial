package eitan.alon.service

import eitan.alon.model.Message
import eitan.alon.model.User
import eitan.alon.model.UserNotFoundException
import eitan.alon.repository.InMemoryMessageRepository
import eitan.alon.repository.InMemoryUserRepository
import java.time.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class WallServiceTest {

    private lateinit var messageRepository: InMemoryMessageRepository
    private lateinit var userRepository: InMemoryUserRepository
    private lateinit var wallService: WallService
    private val baseTime = Instant.parse("2024-01-01T12:00:00Z")

    @BeforeTest
    fun setUp() {
        messageRepository = InMemoryMessageRepository()
        userRepository = InMemoryUserRepository()
        wallService = WallService(messageRepository, userRepository)
    }

    @Test
    fun `wall includes own messages`() {
        userRepository.save(User("Charlie"))
        messageRepository.save(Message("Charlie", "I'm in New York!", baseTime))

        val wall = wallService.getWall("Charlie")

        assertEquals(1, wall.size)
        assertEquals("Charlie", wall[0].author)
    }

    @Test
    fun `wall includes messages from followed users`() {
        userRepository.save(User("Charlie"))
        userRepository.save(User("Alice"))
        messageRepository.save(Message("Charlie", "I'm in New York!", baseTime))
        messageRepository.save(Message("Alice", "I love the weather today", baseTime))
        userRepository.recordFollow("Charlie", "Alice")

        val wall = wallService.getWall("Charlie")

        assertEquals(2, wall.size)
    }

    @Test
    fun `wall excludes messages from non-followed users`() {
        userRepository.save(User("Charlie"))
        userRepository.save(User("Bob"))
        messageRepository.save(Message("Charlie", "I'm in New York!", baseTime))
        messageRepository.save(Message("Bob", "Good game though.", baseTime.plusSeconds(10)))

        val wall = wallService.getWall("Charlie")

        assertEquals(1, wall.size)
        assertEquals("Charlie", wall[0].author)
    }

    @Test
    fun `wall aggregates own and all followed users messages in reverse chronological order`() {
        userRepository.save(User("Alice"))
        userRepository.save(User("Bob"))
        userRepository.save(User("Charlie"))
        messageRepository.save(Message("Alice", "I love the weather", baseTime))
        messageRepository.save(Message("Bob", "Damn! We lost!", baseTime.plusSeconds(60)))
        messageRepository.save(Message("Charlie", "I'm in New York!", baseTime.plusSeconds(120)))
        userRepository.recordFollow("Charlie", "Alice")
        userRepository.recordFollow("Charlie", "Bob")

        val wall = wallService.getWall("Charlie")

        assertEquals(listOf("Charlie", "Bob", "Alice"), wall.map { it.author })
    }

    @Test
    fun `wall for user who follows no one returns only their own messages`() {
        userRepository.save(User("Charlie"))
        userRepository.save(User("Alice"))
        messageRepository.save(Message("Charlie", "I'm in New York!", baseTime))
        messageRepository.save(Message("Alice", "I love the weather today", baseTime.plusSeconds(10)))

        val wall = wallService.getWall("Charlie")

        assertEquals(1, wall.size)
        assertEquals("Charlie", wall[0].author)
    }

    @Test
    fun `throws UserNotFoundException for unknown user`() {
        assertFailsWith<UserNotFoundException> {
            wallService.getWall("NonExistentUser")
        }
    }
}
