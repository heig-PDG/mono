package tupperdate.web.model.chats.firestore

import com.google.cloud.firestore.FieldValue
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.Query
import com.google.cloud.firestore.SetOptions
import io.ktor.http.*
import tupperdate.web.facade.chats.Chat
import tupperdate.web.facade.chats.Message
import tupperdate.web.facade.recipes.Recipe
import tupperdate.web.model.chats.Conversation
import tupperdate.web.model.Result
import tupperdate.web.model.chats.ChatRepository
import tupperdate.web.model.chats.ModelChat
import tupperdate.web.model.chats.ModelMessage
import tupperdate.web.model.chats.ModelNewChat
import tupperdate.web.model.profiles.User
import tupperdate.web.model.profiles.firestore.FirestoreUser
import tupperdate.web.model.profiles.firestore.toModelUser
import tupperdate.web.utils.await
import tupperdate.web.utils.statusException

class FirestoreChatRepository(
    private val store: Firestore,
) : ChatRepository {
    override suspend fun readAll(
        user: User,
    ): Result<List<Conversation>> {
        val smallerId = store.collection("chats").whereEqualTo("userId1", user.id.uid).get()
        val greaterId = store.collection("chats").whereEqualTo("userId2", user.id.uid).get()

        val modelChats = try {
            (smallerId.await().toObjects(FirestoreChat::class.java) +
                    greaterId.await().toObjects(FirestoreChat::class.java))
                .filter { it.user1Recipes != null && it.user2Recipes != null }
                .mapNotNull { it.toModelChat() }
        } catch(throwable: Throwable) {
            return Result.BadServer()
        }

        val convs = modelChats.map {
            val myId = if (user.id.uid == it.user1) it.user1 else it.user2
            val theirId = if (user.id.uid != it.user1) it.user1 else it.user2
            val myRecipes = if (user.id.uid == it.user1) it.user1Recipes else it.user2Recipes
            val theirRecipes = if (user.id.uid != it.user1) it.user1Recipes else it.user2Recipes

            return@map Conversation(
                id = it.identifier,
                myId = myId,
                theirId = theirId,
                myRecipes = myRecipes ?: return Result.BadServer(),
                theirRecipes = theirRecipes ?: return Result.BadServer(),
            )
        }

        return Result.Ok(convs)
    }

    override suspend fun readOne(
        user: User,
        userId: String,
    ): Result<Conversation> {
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
            store.collection("chats").document(chat.id).set(chat, SetOptions.merge()).await()
            Result.Ok(Unit)
        } catch (throwable: Throwable) {
            Result.BadServer()
        }
    }

    override suspend fun updateLikes(chatId: String, likes: Map<String, String>): Result<Unit> {
        return try {
            val firestoreLikes = likes.mapValues { kv -> FieldValue.arrayUnion(kv.value) }
            store.collection("chats").document(chatId).update(firestoreLikes).await()
            Result.Ok(Unit)
        } catch (throwable: Throwable) {
            Result.BadServer()
        }
    }

}
