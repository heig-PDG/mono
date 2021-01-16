package tupperdate.web.model.chats

import com.google.cloud.firestore.FieldValue
import tupperdate.web.model.Result
import tupperdate.web.model.profiles.User

interface ChatRepository {

    /**
     * Read all chats of an user
     */
    suspend fun readAll(
        user: User,
    ): Result<List<ModelChat>>

    /**
     * Read one chat of one user
     */
    suspend fun readOne(
        user: User,
        otherUserId: String,
    ): Result<ModelChat>

    /**
     * Read all messages in a chat
     */
    suspend fun readMessages(
        user: User,
        otherUserId: String,
    ): Result<List<ModelMessage>>

    /**
     * Send a message to another user
     */
    suspend fun sendMessage(
        user: User,
        otherUserId: String,
        content: String,
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
