package eitan.alon.model

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserTest {
    private lateinit var user: User

    @BeforeTest
    fun setUp() {
        user = User("Alice")
    }

    @Test
    fun `followers is empty on creation`() {
        assertTrue(user.followers.isEmpty())
    }

    @Test
    fun `followees is empty on creation`() {
        assertTrue(user.followees.isEmpty())
    }

    @Test
    fun `addFollower adds to followers set`() {
        user.addFollower("Bob")

        assertEquals(setOf("Bob"), user.followers)
    }

    @Test
    fun `addFollowee adds to followees set`() {
        user.addFollowee("Bob")

        assertEquals(setOf("Bob"), user.followees)
    }

    @Test
    fun `addFollower is idempotent`() {
        user.addFollower("Bob")
        user.addFollower("Bob")

        assertEquals(1, user.followers.size)
    }

    @Test
    fun `addFollowee is idempotent`() {
        user.addFollowee("Bob")
        user.addFollowee("Bob")

        assertEquals(1, user.followees.size)
    }

    @Test
    fun `followers and followees are independent sets`() {
        user.addFollower("Charlie")
        user.addFollowee("Diana")

        assertEquals(setOf("Charlie"), user.followers)
        assertEquals(setOf("Diana"), user.followees)
    }
}
