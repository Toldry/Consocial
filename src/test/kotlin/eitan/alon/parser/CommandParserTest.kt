package eitan.alon.parser

import eitan.alon.model.Command
import eitan.alon.model.User.Companion.MAX_USERNAME_LENGTH
import eitan.alon.model.User.Companion.MIN_USERNAME_LENGTH
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CommandParserTest {
    private val parser = CommandParser()

    // --- Posting ---

    @Test
    fun `parses posting command`() {
        assertEquals(
            Command.Post("Alice", "I love the weather today"),
            parser.parse("Alice -> I love the weather today"),
        )
    }

    @Test
    fun `parses posting command with whitespaces`() {
        assertEquals(
            Command.Post("Alice", "I love the weather today"),
            parser.parse("  Alice    ->    I love the weather today"),
        )
    }

    @Test
    fun `parses posting command with punctuation in message`() {
        assertEquals(
            Command.Post("Bob", "Damn! We lost!"),
            parser.parse("Bob -> Damn! We lost!"),
        )
    }

    @Test
    fun `parses posting command whose message contains the word follows`() {
        assertEquals(
            Command.Post("Alice", "Charlie follows me everywhere"),
            parser.parse("Alice -> Charlie follows me everywhere"),
        )
    }

    @Test
    fun `parses posting command whose message contains the word wall`() {
        assertEquals(
            Command.Post("Alice", "Hit against a wall"),
            parser.parse("Alice -> Hit against a wall"),
        )
    }

    @Test
    fun `parses posting command whose message contains arrow`() {
        assertEquals(
            Command.Post("Alice", "a -> b -> c"),
            parser.parse("Alice -> a -> b -> c"),
        )
    }

    @Test
    fun `throws IllegalArgumentException post command without username`() {
        assertFailsWith<IllegalArgumentException> {
            parser.parse("->")
        }
    }

    @Test
    fun `throws IllegalArgumentException for empty message after arrow`() {
        assertFailsWith<IllegalArgumentException> {
            parser.parse("Charlie ->")
        }
    }

    @Test
    fun `throws IllegalArgumentException for whitespace-only message after arrow`() {
        assertFailsWith<IllegalArgumentException> {
            parser.parse("Charlie ->   ")
        }
    }

    @Test
    fun `throws IllegalArgumentException for non-alphanumeric username`() {
        assertFailsWith<IllegalArgumentException> {
            parser.parse("Al!ce -> I have an excalmation mark in my username")
        }
    }

    // --- Reading ---

    @Test
    fun `parses reading command`() {
        assertEquals(Command.Read("Alice"), parser.parse("Alice"))
    }

    @Test
    fun `parses reading command with whitespace`() {
        assertEquals(Command.Read("Alice"), parser.parse("    Alice  "))
    }

    // --- Following ---

    @Test
    fun `parses following command`() {
        assertEquals(
            Command.Follow("Charlie", "Alice"),
            parser.parse("Charlie follows Alice"),
        )
    }

    @Test
    fun `user attempts to follow themselves`() {
        assertFailsWith<IllegalArgumentException> {
            Command.Follow("Charlie", "Charlie")
        }
    }

    @Test
    fun `parses following command with whitespaces`() {
        assertEquals(
            Command.Follow("Alice", "Charlie"),
            parser.parse("    Alice  follows    Charlie  "),
        )
    }

    // --- Wall ---

    @Test
    fun `parses wall command`() {
        assertEquals(Command.Wall("Charlie"), parser.parse("Charlie wall"))
    }

    @Test
    fun `parses wall command with whitespace`() {
        assertEquals(
            Command.Wall("Charlie"),
            parser.parse("       Charlie   wall "),
        )
    }

    // --- Username validation ---

    @Test
    fun `throws IllegalArgumentException for username shorter than MIN_USERNAME_LENGTH (post)`() {
        assertFailsWith<IllegalArgumentException> {
            parser.parse("${"A".repeat(MIN_USERNAME_LENGTH - 1)} -> Hello")
        }
    }

    @Test
    fun `throws IllegalArgumentException for username longer than MAX_USERNAME_LENGTH (post)`() {
        assertFailsWith<IllegalArgumentException> {
            parser.parse("${"A".repeat(MAX_USERNAME_LENGTH + 1)} -> Hello")
        }
    }

    @Test
    fun `throws IllegalArgumentException for username shorter than MIN_USERNAME_LENGTH (read)`() {
        assertFailsWith<IllegalArgumentException> {
            parser.parse("A".repeat(MIN_USERNAME_LENGTH - 1))
        }
    }

    @Test
    fun `throws IllegalArgumentException for username longer than MAX_USERNAME_LENGTH (read)`() {
        assertFailsWith<IllegalArgumentException> {
            parser.parse("A".repeat(MAX_USERNAME_LENGTH + 1))
        }
    }

    @Test
    fun `throws IllegalArgumentException for follower username shorter than MIN_USERNAME_LENGTH`() {
        assertFailsWith<IllegalArgumentException> {
            parser.parse("${"A".repeat(MIN_USERNAME_LENGTH - 1)} follows Alice")
        }
    }

    @Test
    fun `throws IllegalArgumentException for follower username longer than MAX_USERNAME_LENGTH`() {
        assertFailsWith<IllegalArgumentException> {
            parser.parse("${"A".repeat(MAX_USERNAME_LENGTH + 1)} follows Alice")
        }
    }

    @Test
    fun `throws IllegalArgumentException for followee username shorter than MIN_USERNAME_LENGTH`() {
        assertFailsWith<IllegalArgumentException> {
            parser.parse("Alice follows ${"A".repeat(MIN_USERNAME_LENGTH - 1)}")
        }
    }

    @Test
    fun `throws IllegalArgumentException for followee username longer than MAX_USERNAME_LENGTH`() {
        assertFailsWith<IllegalArgumentException> {
            parser.parse("Alice follows ${"A".repeat(MAX_USERNAME_LENGTH + 1)}")
        }
    }

    @Test
    fun `throws IllegalArgumentException for username shorter than MIN_USERNAME_LENGTH (wall)`() {
        assertFailsWith<IllegalArgumentException> {
            parser.parse("${"A".repeat(MIN_USERNAME_LENGTH - 1)} wall")
        }
    }

    @Test
    fun `throws IllegalArgumentException for username longer than MAX_USERNAME_LENGTH (wall)`() {
        assertFailsWith<IllegalArgumentException> {
            parser.parse("${"A".repeat(MAX_USERNAME_LENGTH + 1)} wall")
        }
    }

    // --- General invalid input ---

    @Test
    fun `throws IllegalArgumentException for empty string`() {
        assertFailsWith<IllegalArgumentException> {
            parser.parse("")
        }
    }

    @Test
    fun `throws IllegalArgumentException for unrecognised input`() {
        assertFailsWith<IllegalArgumentException> {
            parser.parse("???")
        }
    }
}
