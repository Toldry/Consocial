package eitan.alon.service

import eitan.alon.model.Message
import eitan.alon.model.UserNotFoundException
import eitan.alon.repository.MessageRepository

class TimelineService(private val messageRepository: MessageRepository) {

    /**
     * Returns all messages posted by [username], sorted newest-first.
     *
     * @param username the user whose timeline to retrieve
     * @return messages in reverse chronological order
     * @throws UserNotFoundException if [username] has never posted a message
     */
    fun getTimeline(username: String): List<Message> = TODO()
}
