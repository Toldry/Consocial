package eitan.alon.repository

import eitan.alon.model.User
import eitan.alon.model.UserNotFoundException

/** In-memory implementation of [UserRepository] backed by a mutable map. */
class InMemoryUserRepository : UserRepository {
    private val users = mutableMapOf<String, User>()

    override fun save(user: User) {
        users[user.username] = user
    }

    override fun findByUsername(username: String): User = users[username] ?: throw UserNotFoundException(username)

    override fun recordFollow(
        followerUsername: String,
        followeeUsername: String,
    ) {
        val follower = findByUsername(followerUsername)
        val followee = findByUsername(followeeUsername)
        follower.addFollowee(followeeUsername)
        followee.addFollower(followerUsername)
    }
}
