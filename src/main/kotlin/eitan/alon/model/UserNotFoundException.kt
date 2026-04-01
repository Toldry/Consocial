package eitan.alon.model

/**
 * Thrown when an operation is requested for a user who has never posted a message.
 *
 * @param username the username that could not be found
 */
class UserNotFoundException(val username: String) : RuntimeException("User not found: '$username'")
