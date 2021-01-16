package tupperdate.web.facade.chats

import tupperdate.web.model.Result
import tupperdate.web.model.profiles.User

interface ChatFacade {
    /**
     * Fetch all chats of an user
     */
    suspend fun readAll(
        user: User,
    ): Result<List<Chat>>

    /**
     * Fetch one of all chats of an user
     */
    suspend fun readOne(
        user: User,
        userId: String,
    ): Result<Chat>

    /**
     * Mark all messages as "read"
     */
    suspend fun readMessages(
        user: User,
        userId: String,
    ): Result<List<Message>>

    /**
     * Send a message to another user
     */
    suspend fun sendMessage(
        user: User,
        userId: String,
        newMessage: NewMessage,
    ): Result<Unit>
}
