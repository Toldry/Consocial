package eitan.alon.model

/**
 * Represents a registered user of the social network.
 *
 * Follow relationships are bidirectional: when user A follows user B,
 * A's [followees] gains B and B's [followers] gains A.
 * Both sides are updated atomically via [eitan.alon.repository.UserRepository.recordFollow].
 *
 * @property username the unique alphanumeric identifier for this user
 */
class User(val username: String) {

    companion object {
        /** Minimum number of characters in a valid username. */
        const val MIN_USERNAME_LENGTH = 2

        /** Maximum number of characters in a valid username. */
        const val MAX_USERNAME_LENGTH = 30
    }

    private val _followers = mutableSetOf<String>()
    private val _followees = mutableSetOf<String>()

    /** Usernames of users who follow this user. */
    val followers: Set<String> get() = _followers.toSet()

    /** Usernames of users this user follows. */
    val followees: Set<String> get() = _followees.toSet()

    /**
     * Records that [followerUsername] now follows this user.
     * Called exclusively by [eitan.alon.repository.UserRepository.recordFollow].
     *
     * @param followerUsername the username of the user who is following this user
     */
    internal fun addFollower(followerUsername: String) {
        _followers.add(followerUsername)
    }

    /**
     * Records that this user now follows [followeeUsername].
     * Called exclusively by [eitan.alon.repository.UserRepository.recordFollow].
     *
     * @param followeeUsername the username of the user being followed
     */
    internal fun addFollowee(followeeUsername: String) {
        _followees.add(followeeUsername)
    }


}
