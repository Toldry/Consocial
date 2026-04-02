package eitan.alon.repository

import eitan.alon.model.User
import eitan.alon.model.UserNotFoundException

/** Persistence contract for [User] entities and their follow relationships. */
interface UserRepository {
    /**
     * Persists [user], overwriting any existing entry for the same username.
     *
     * @param user the user to store
     */
    fun save(user: User)

    /**
     * Returns the [User] with the given [username].
     *
     * @param username the username to look up
     * @return the matching [User]
     * @throws UserNotFoundException if no user with [username] has been saved
     */
    fun findByUsername(username: String): User

    /**
     * Adds [followeeUsername] to the follower's followees and [followerUsername]
     * to the followee's followers. If the relationship already exists, this is a no-op.
     *
     * @param followerUsername the user who is subscribing
     * @param followeeUsername the user being subscribed to
     * @throws UserNotFoundException if either username is not found
     */
    fun recordFollow(
        followerUsername: String,
        followeeUsername: String,
    )
}
