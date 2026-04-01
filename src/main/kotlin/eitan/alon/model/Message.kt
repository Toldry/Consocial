package eitan.alon.model

import java.time.Instant

/**
 * Represents a message posted by a user on their personal timeline.
 *
 * @property author the username of the person who posted the message
 * @property content the text body of the message
 * @property postedAt the instant at which the message was posted
 */
data class Message(
    val author: String,
    val content: String,
    val postedAt: Instant,
)
