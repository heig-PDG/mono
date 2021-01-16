package tupperdate.web.model.chats.firestore

import io.ktor.http.*
import tupperdate.common.dto.MessageDTO
import tupperdate.web.model.chats.ModelChat
import tupperdate.web.model.chats.ModelMessage
import tupperdate.web.utils.statusException

data class FirestoreChat(
    val id: String? = null,
    val userId1: String? = null,
    val userId2: String? = null,
    val user1Recipes: List<String>? = null,
    val user2Recipes: List<String>? = null,
)

data class FirestoreMessage(
    val id: String? = null,
    val tempId: String? = null,
    val content: String? = null,
    val timestamp: Long? = null,
    val fromUser: String? = null,
)

fun FirestoreChat.toModelChat(): ModelChat? {
    return ModelChat(
        identifier = id ?: return null,
        user1 = userId1 ?: return null,
        user2 = userId2 ?: return null,
        user1Recipes = user1Recipes,
        user2Recipes = user2Recipes,
    )
}

fun FirestoreMessage.toModelMessage(): ModelMessage? {
    return ModelMessage(
        identifier = id ?: return null,
        tempId = tempId ?: return null,
        content = content ?: return null,
        timestamp = timestamp ?: return null,
        senderId = fromUser ?: return null,
    )
}
