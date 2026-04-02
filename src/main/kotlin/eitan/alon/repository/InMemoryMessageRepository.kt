package eitan.alon.repository

import eitan.alon.model.Message
import eitan.alon.model.UserNotFoundException

/** In-memory implementation of [MessageRepository] backed by a map of author to messages. */
class InMemoryMessageRepository : MessageRepository {
    private val messages = mutableMapOf<String, MutableList<Message>>()

    override fun save(message: Message) {
        messages.getOrPut(message.author) { mutableListOf() }.add(message)
    }

    override fun findByAuthor(author: String): List<Message> = messages[author] ?: throw UserNotFoundException(author)
}
