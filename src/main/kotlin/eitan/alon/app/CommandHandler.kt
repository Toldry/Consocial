package eitan.alon.app

import eitan.alon.clock.Clock
import eitan.alon.model.Command
import eitan.alon.model.Message
import eitan.alon.model.User
import eitan.alon.model.UserNotFoundException
import eitan.alon.repository.MessageRepository
import eitan.alon.repository.UserRepository
import eitan.alon.service.TimelineService
import eitan.alon.service.WallService
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Executes [Command] objects and formats output for the console.
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
    fun handleCommand(command: Command): String? = when (command) {
        is Command.Post -> {
            try {
                userRepository.findByUsername(command.username)
            } catch (_: UserNotFoundException) {
                userRepository.save(User(command.username))
            }
            messageRepository.save(Message(command.username, command.message, clock.now()))
            null
        }
        is Command.Follow -> {
            try {
                userRepository.recordFollow(command.follower, command.followee)
                null
            } catch (e: UserNotFoundException) {
                e.message
            }
        }
        is Command.Read -> {
            try {
                val now = clock.now()
                timelineService.getTimeline(command.username)
                    .joinToString("\n") { "${it.content} (${formatTime(it.postedAt, now)})" }
            } catch (e: UserNotFoundException) {
                e.message
            }
        }
        is Command.Wall -> {
            try {
                val now = clock.now()
                wallService.getWall(command.username)
                    .joinToString("\n") { "${it.author} - ${it.content} (${formatTime(it.postedAt, now)})" }
            } catch (e: UserNotFoundException) {
                e.message
            }
        }
    }

    /**
     * Formats the duration between [postedAt] and [now] as a human-readable relative string,
     * e.g. `"2 minutes ago"` or `"1 second ago"`.
     *
     * @param postedAt the instant the message was posted
     * @param now the current instant
     * @return a relative time string
     */
    private fun formatTime(postedAt: Instant, now: Instant): String {
        val days = ChronoUnit.DAYS.between(postedAt, now)
        val hours = ChronoUnit.HOURS.between(postedAt, now)
        val minutes = ChronoUnit.MINUTES.between(postedAt, now)
        val seconds = ChronoUnit.SECONDS.between(postedAt, now)
        val (count, unit) = when {
            days >= 1 -> days to "day"
            hours >= 1 -> hours to "hour"
            minutes >= 1 -> minutes to "minute"
            else -> seconds to "second"
        }
        return "$count ${if (count == 1L) unit else "${unit}s"} ago"
    }
}
