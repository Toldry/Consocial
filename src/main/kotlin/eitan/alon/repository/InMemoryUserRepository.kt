package eitan.alon.repository

import eitan.alon.model.User

/** In-memory implementation of [UserRepository] backed by a mutable map. */
class InMemoryUserRepository : UserRepository {
    private val users = mutableMapOf<String, User>()

    override fun save(user: User): Unit = TODO()

    override fun findByUsername(username: String): User = TODO()

    override fun recordFollow(followerUsername: String, followeeUsername: String): Unit = TODO()
}
