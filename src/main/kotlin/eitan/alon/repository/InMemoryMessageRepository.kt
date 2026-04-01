package eitan.alon.repository

import eitan.alon.model.Message

/** In-memory implementation of [MessageRepository] backed by a mutable list. */
class InMemoryMessageRepository : MessageRepository {
    private val messages = mutableListOf<Message>()

    override fun save(message: Message): Unit = TODO()

    override fun findByAuthor(author: String): List<Message> = TODO()
}
