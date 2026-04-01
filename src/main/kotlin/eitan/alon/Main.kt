package eitan.alon

import eitan.alon.app.CommandHandler
import eitan.alon.app.SocialNetworkApp
import eitan.alon.clock.SystemClock
import eitan.alon.parser.CommandParser
import eitan.alon.repository.InMemoryMessageRepository
import eitan.alon.repository.InMemoryUserRepository
import eitan.alon.service.TimelineService
import eitan.alon.service.WallService

fun main() {
    val clock = SystemClock()
    val messageRepository = InMemoryMessageRepository()
    val userRepository = InMemoryUserRepository()
    val handler = CommandHandler(
        messageRepository = messageRepository,
        userRepository = userRepository,
        timelineService = TimelineService(messageRepository),
        wallService = WallService(messageRepository, userRepository),
        clock = clock,
    )
    SocialNetworkApp(handler, CommandParser()).run()
}
