package eitan.alon.parser

import eitan.alon.model.Command

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
     * @throws eitan.alon.model.UserNotFoundException if [input] queries a username that does not exist
     */
    fun parse(input: String): Command = TODO()
}
