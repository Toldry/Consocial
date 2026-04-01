package eitan.alon.model

sealed class Command {

    /**
     * Posts a message to [username]'s personal timeline.
     *
     * @property username the author of the message
     * @property message the text content to post
     */
    data class Post(val username: String, val message: String) : Command()

    /**
     * Reads and displays [username]'s personal timeline.
     *
     * @property username the user whose timeline to display
     */
    data class Read(val username: String) : Command()

    /**
     * Subscribes [follower] to [followee]'s timeline.
     *
     * @property follower the user who is subscribing
     * @property followee the user being subscribed to
     */
    data class Follow(val follower: String, val followee: String) : Command() {
        init {
            require(follower != followee) { "A user cannot follow themselves" }
        }
    }

    /**
     * Displays the aggregated wall of [username] and all users they follow.
     *
     * @property username the user whose wall to display
     */
    data class Wall(val username: String) : Command()
}
