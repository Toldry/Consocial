package eitan.alon.parser

import eitan.alon.model.Command
import eitan.alon.model.User

/**
 * Parses raw console input lines into typed [Command] objects.
 *
 * Supported patterns (evaluated in priority order):
 * - `"Alice -> message"` → [Command.Post]
 * - `"Charlie follows Bob"` → [Command.Follow]
 * - `"Charlie wall"` → [Command.Wall]
 * - `"Alice"` → [Command.Read]
 */
class CommandParser {

    private companion object {
        val POST_PATTERN = Regex("""^(\S+)\s*->\s*(.+)$""")
        val FOLLOW_PATTERN = Regex("""^(\S+)\s+follows\s+(\S+)$""")
        val WALL_PATTERN = Regex("""^(\S+)\s+wall$""")
    }

    /**
     * Parses a raw console input string into the corresponding [Command].
     *
     * Usernames must be alphanumeric and between [User.MIN_USERNAME_LENGTH] and
     * [User.MAX_USERNAME_LENGTH] characters inclusive.
     *
     * @param input the raw line entered by the user
     * @return the parsed [Command]
     * @throws IllegalArgumentException if [input] does not match any known pattern,
     *   or if any username fails validation
     */
    fun parse(input: String): Command {
        val trimmed = input.trim()
        require(trimmed.isNotEmpty()) { "Input must not be empty" }

        POST_PATTERN.matchEntire(trimmed)?.let { match ->
            val message = match.groupValues[2].trim()
            require(message.isNotEmpty()) { "Post message must not be empty" }
            return Command.Post(validateUsername(match.groupValues[1]), message)
        }

        FOLLOW_PATTERN.matchEntire(trimmed)?.let { match ->
            return Command.Follow(
                validateUsername(match.groupValues[1]),
                validateUsername(match.groupValues[2]),
            )
        }

        WALL_PATTERN.matchEntire(trimmed)?.let { match ->
            return Command.Wall(validateUsername(match.groupValues[1]))
        }

        return Command.Read(validateUsername(trimmed))
    }

    private fun validateUsername(username: String): String {
        require(username.length >= User.MIN_USERNAME_LENGTH) {
            "Username must be at least ${User.MIN_USERNAME_LENGTH} characters"
        }
        require(username.length <= User.MAX_USERNAME_LENGTH) {
            "Username must be at most ${User.MAX_USERNAME_LENGTH} characters"
        }
        require(username.all { it.isLetterOrDigit() }) {
            "Username must be alphanumeric"
        }
        return username
    }
}
