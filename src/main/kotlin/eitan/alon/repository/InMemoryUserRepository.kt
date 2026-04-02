package eitan.alon.repository

import eitan.alon.model.User
import eitan.alon.model.UserNotFoundException

/** In-memory implementation of [UserRepository] backed by a mutable map. */
class InMemoryUserRepository : UserRepository {
    private val users = mutableMapOf<String, User>()

    override fun save(user: User) {
        users[user.username] = user
    }

    override fun findByUsername(username: String): User? = users[username]

    override fun recordFollow(
        followerUsername: String,
        followeeUsername: String,
    ) {
        val follower = findByUsername(followerUsername) ?: throw UserNotFoundException(followerUsername)
        val followee = findByUsername(followeeUsername) ?: throw UserNotFoundException(followeeUsername)
        follower.addFollowee(followeeUsername)
        followee.addFollower(followerUsername)
    }
}
