package tupperdate.web.model.chats.firestore

import com.google.cloud.firestore.FieldValue
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.Query
import com.google.cloud.firestore.SetOptions
import io.ktor.http.*
import tupperdate.web.model.Result
import tupperdate.web.model.chats.ChatRepository
import tupperdate.web.model.chats.Conversation
import tupperdate.web.model.chats.ModelMessage
import tupperdate.web.model.chats.ModelNewChat
import tupperdate.web.model.profiles.User
import tupperdate.web.utils.await

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
        } catch (throwable: Throwable) {
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
                myRecipes = myRecipes ?: return Result.NotFound(),
                theirRecipes = theirRecipes ?: return Result.NotFound(),
            )
        }

        return Result.Ok(convs)
    }

    override suspend fun readOne(
        user: User,
        userId: String,
    ): Result<Conversation> {
        val chatId = minOf(user.id.uid, userId) + "_" + maxOf(user.id.uid, userId)
        val modelChat = try {
            store.collection("chats").document(chatId).get().await()
                .toObject(FirestoreChat::class.java)?.toModelChat() ?: return Result.NotFound()
        } catch (throwable: Throwable) {
            return Result.BadServer()
        }

        val myId = if (user.id.uid == modelChat.user1) modelChat.user1 else modelChat.user2
        val theirId = if (user.id.uid != modelChat.user1) modelChat.user1 else modelChat.user2
        val myRecipes =
            if (user.id.uid == modelChat.user1) modelChat.user1Recipes else modelChat.user2Recipes
        val theirRecipes =
            if (user.id.uid != modelChat.user1) modelChat.user1Recipes else modelChat.user2Recipes

        return Result.Ok(
            Conversation(
                id = modelChat.identifier,
                myId = myId,
                theirId = theirId,
                myRecipes = myRecipes ?: return Result.NotFound(),
                theirRecipes = theirRecipes ?: return Result.NotFound(),
            )
        )
    }

    override suspend fun readMessages(
        user: User,
        userId: String,
    ): Result<List<ModelMessage>> {
        val chatId = minOf(user.id.uid, userId) + "_" + maxOf(user.id.uid, userId)

        val modelChat = try {
            store.collection("chats").document(chatId).get().await()
                .toObject(FirestoreChat::class.java)?.toModelChat() ?: return Result.NotFound()
        } catch (throwable: Throwable) {
            return Result.BadServer()
        }

        if (modelChat.user1Recipes == null || modelChat.user2Recipes == null) {
            return Result.NotFound()
        }

        return try {
            val messages = store.collection("chats").document(chatId).collection("messages")
                .orderBy("timestamp", Query.Direction.DESCENDING).get().await()
                .toObjects(FirestoreMessage::class.java).mapNotNull { it.toModelMessage() }
            Result.Ok(messages)
        } catch(throwable: Throwable) {
            Result.BadServer()
        }
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
