package tupperdate.web.model.chats.firestore

import com.google.cloud.firestore.FieldValue
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.SetOptions
import tupperdate.web.model.Result
import tupperdate.web.model.chats.ChatRepository
import tupperdate.web.model.chats.ModelChat
import tupperdate.web.model.chats.ModelMessage
import tupperdate.web.model.chats.ModelNewChat
import tupperdate.web.model.profiles.User

class FirestoreChatRepository(
    private val store: Firestore,
) : ChatRepository {
    override suspend fun readAll(
        user: User,
    ): Result<List<ModelChat>> {
        TODO("Not yet implemented")
    }

    override suspend fun readOne(
        user: User,
        otherUserId: String,
    ): Result<ModelChat> {
        TODO("Not yet implemented")
    }

    override suspend fun readMessages(
        user: User,
        otherUserId: String,
    ): Result<List<ModelMessage>> {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(
        user: User,
        otherUserId: String,
        content: String,
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun saveNewChat(chat: ModelNewChat): Result<Unit> {
        return try {
            store.collection("chats").document(chat.id).set(chat, SetOptions.merge())
            Result.Ok(Unit)
        } catch (throwable: Throwable) {
            Result.BadServer()
        }
    }

    override suspend fun updateLikes(chatId: String, likes: Map<String, FieldValue>): Result<Unit> {
        return try {
            store.collection("chats").document(chatId).update(likes)
            Result.Ok(Unit)
        } catch (throwable: Throwable) {
            Result.BadServer()
        }
    }

}
