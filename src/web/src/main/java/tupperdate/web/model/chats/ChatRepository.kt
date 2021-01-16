package tupperdate.web.model.chats

import tupperdate.web.model.Result
import tupperdate.web.model.profiles.User

interface ChatRepository {

    /**
     * Read all chats of an user
     */
    suspend fun readAll(
        user: User,
    ): Result<List<Conversation>>

    /**
     * Read one chat of one user
     */
    suspend fun readOne(
        user: User,
        userId: String,
    ): Result<Conversation>

    /**
     * Read last message in a chat
     */
    suspend fun readLastMessages(
        user: User,
        userId: String,
    ): Result<ModelMessage>

    /**
     * Read all messages in a chat
     */
    suspend fun readMessages(
        user: User,
        userId: String,
    ): Result<List<ModelMessage>>

    /**
     * Send a message to another user
     */
    suspend fun sendMessage(
        user: User,
        userId: String,
        newMessage: ModelNewMessage,
    ): Result<Unit>

    /**
     * Create and register a new chat
     */
    suspend fun saveNewChat(
        chat: ModelNewChat,
    ): Result<Unit>

    /**
     * Update new chats list according to new matches
     */
    suspend fun updateLikes(
        chatId: String,
        likes: Map<String, String>,
    ): Result<Unit>
}
