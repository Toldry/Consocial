package eitan.alon.repository

import eitan.alon.model.Message

interface MessageRepository {
    /**
     * Persists a [message].
     *
     * @param message the message to store
     */
    fun save(message: Message)

    /**
     * Returns all messages posted by [author], in insertion order.
     *
     * @param author the username to query
     * @return a list of messages posted by [author]
     * @throws eitan.alon.model.UserNotFoundException if [author] has not been saved
     */
    fun findByAuthor(author: String): List<Message>
}
