package eitan.alon.repository

import eitan.alon.model.User
import eitan.alon.model.UserNotFoundException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class InMemoryUserRepositoryTest {
    private lateinit var repository: InMemoryUserRepository

    @BeforeTest
    fun setUp() {
        repository = InMemoryUserRepository()
    }

    @Test
    fun `findByUsername returns null for unknown user`() {
        assertNull(repository.findByUsername("Alice"))
    }

    @Test
    fun `findByUsername returns saved user`() {
        repository.save(User("Alice"))

        assertEquals("Alice", repository.findByUsername("Alice")!!.username)
    }

    @Test
    fun `recordFollow adds followee to follower and follower to followee`() {
        repository.save(User("Alice"))
        repository.save(User("Bob"))

        repository.recordFollow("Alice", "Bob")

        val alice = repository.findByUsername("Alice")!!
        val bob = repository.findByUsername("Bob")!!
        assertTrue("Bob" in alice.followees)
        assertTrue("Alice" in bob.followers)
    }

    @Test
    fun `recordFollow is idempotent`() {
        repository.save(User("Alice"))
        repository.save(User("Bob"))

        repository.recordFollow("Alice", "Bob")
        repository.recordFollow("Alice", "Bob")

        assertEquals(1, repository.findByUsername("Alice")!!.followees.size)
        assertEquals(1, repository.findByUsername("Bob")!!.followers.size)
    }

    @Test
    fun `recordFollow throws UserNotFoundException for unknown follower`() {
        repository.save(User("Bob"))

        assertFailsWith<UserNotFoundException> {
            repository.recordFollow("Unknown", "Bob")
        }
    }

    @Test
    fun `recordFollow throws UserNotFoundException for unknown followee`() {
        repository.save(User("Alice"))

        assertFailsWith<UserNotFoundException> {
            repository.recordFollow("Alice", "Unknown")
        }
    }
}
