package eitan.alon.service

import eitan.alon.model.Message
import eitan.alon.model.UserNotFoundException
import eitan.alon.repository.MessageRepository
import eitan.alon.repository.UserRepository

/**
 * Aggregates messages from a user and all users they follow into a single wall view.
 *
 * @property messageRepository repository for retrieving posted messages
 * @property userRepository repository for resolving users and their follow relationships
 */
class WallService(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
) {
    /**
     * Returns the aggregated wall for [username], including their own messages
     * and those of every user they follow, sorted newest-first.
     *
     * @param username the user whose wall to retrieve
     * @return messages from the user and their followees in reverse chronological order
     * @throws UserNotFoundException if [username] has never been registered
     */
    fun getWall(username: String): List<Message> {
        val user = userRepository.findByUsername(username) ?: throw UserNotFoundException(username)
        val usernames = setOf(username) + user.followees
        return usernames
            .flatMap { name ->
                try {
                    messageRepository.findByAuthor(name)
                } catch (_: UserNotFoundException) {
                    emptyList()
                }
            }
            .sortedByDescending { it.postedAt }
    }
}
