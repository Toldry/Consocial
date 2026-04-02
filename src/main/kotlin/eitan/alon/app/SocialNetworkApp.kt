package eitan.alon.app

import eitan.alon.parser.CommandParser
import kotlin.system.exitProcess

/**
 * Main application loop that drives the social network via console commands.
 *
 * @property commandHandler executes [eitan.alon.model.Command] objects and returns formatted output
 * @property parser parses raw console input lines into typed commands
 */
class SocialNetworkApp(
    private val commandHandler: CommandHandler,
    private val parser: CommandParser,
) {
    /**
     * Starts the interactive console loop.
     *
     * Reads lines from stdin, parses each via [parser], delegates execution to
     * [commandHandler], and prints any output until the stream is closed.
     */
    fun run(): Nothing {
        println(
            """
            Welcome to Consocial, the console-based social networking app.

            Commands:
              <user> -> <message>        Post a message
              <user>                     Read a user's timeline
              <user> follows <user>      Follow a user
              <user> wall                View a user's wall (own posts + followed users)

            Press Ctrl+C to exit.
            """.trimIndent(),
        )
        while (true) {
            print("> ")
            val line = readlnOrNull() ?: exitProcess(0)
            try {
                val output = commandHandler.handleCommand(parser.parse(line))
                if (output != null) println(output)
            } catch (e: IllegalArgumentException) {
                println("Error: ${e.message}")
            }
        }
    }
}
