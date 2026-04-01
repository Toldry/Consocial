package eitan.alon.app

import eitan.alon.parser.CommandParser

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
    fun run(): Nothing = TODO()
}
