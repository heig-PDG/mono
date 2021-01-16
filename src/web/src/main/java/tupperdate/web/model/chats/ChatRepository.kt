package tupperdate.web.model.chats

import tupperdate.web.model.Result
import tupperdate.web.model.profiles.User

interface ChatRepository {

    suspend fun readAll(
        user: User,
    ): Result<List<Conversation>>

    suspend fun readOne(
        user: User,
        userId: String,
    ): Result<Conversation>

    suspend fun readMessages(
        user: User,
        userId: String,
    ): Result<List<ModelMessage>>

    suspend fun sendMessage(
        user: User,
        userId: String,
        newMessage: ModelNewMessage,
    ): Result<Unit>

    suspend fun saveNewChat(
        chat: ModelNewChat,
    ): Result<Unit>

    suspend fun updateLikes(
        chatId: String,
        likes: Map<String, String>,
    ): Result<Unit>
}
