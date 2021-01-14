package tupperdate.web.model.chats.firestore

import io.ktor.http.*
import tupperdate.common.dto.MessageDTO
import tupperdate.web.legacy.exceptions.statusException

data class FirestoreChat (
    val id: String? = null,
    val userId1 : String? = null,
    val userId2 : String? = null,
    val user1Recipes : List<String>? = null,
    val user2Recipes : List<String>? = null,
)

data class FirestoreMessage (
    val id: String? = null,
    val tempId: String? = null,
    val content: String? = null,
    val timestamp: Long? = null,
    val fromUser: String? = null,
)

fun FirestoreMessage.toMessageDTO() : MessageDTO {

    return MessageDTO(
        id = this.id ?: statusException(HttpStatusCode.InternalServerError),
        tempId = this.tempId ?: statusException(HttpStatusCode.InternalServerError),
        senderId = this.fromUser ?: statusException(HttpStatusCode.InternalServerError),
        timestamp = this.timestamp ?: statusException(HttpStatusCode.InternalServerError),
        content = this.content ?: statusException(HttpStatusCode.InternalServerError),
    )
}
