package eitan.alon.app

import eitan.alon.clock.Clock
import eitan.alon.model.Command
import eitan.alon.repository.MessageRepository
import eitan.alon.repository.UserRepository
import eitan.alon.service.TimelineService
import eitan.alon.service.WallService

/**
 * Executes [Command] objects against the domain layer and formats output for the console.
 *
 * @property messageRepository repository for persisting and querying messages
 * @property userRepository repository for persisting users and recording follow relationships
 * @property timelineService service for retrieving a user's personal timeline
 * @property wallService service for retrieving a user's aggregated wall
 * @property clock clock used to compute relative timestamps
 */
class CommandHandler(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val timelineService: TimelineService,
    private val wallService: WallService,
    private val clock: Clock,
) {

    /**
     * Executes [command] and returns output to be printed to the console.
     *
     * @param command the command to execute
     * @return a formatted string to display, or `null` if the command produces no output
     */
    fun handleCommand(command: Command): String? = TODO()
}
