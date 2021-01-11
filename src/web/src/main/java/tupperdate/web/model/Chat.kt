package tupperdate.web.model

import io.ktor.http.*
import tupperdate.common.dto.MessageDTO
import tupperdate.web.exceptions.statusException

data class Chat (
    val id: String? = null,
    val userId1 : String? = null,
    val userId2 : String? = null,
    val user1Recipes : List<String>? = null,
    val user2Recipes : List<String>? = null,
)

data class Conv (
    val id: String,
    val myId : String,
    val theirId : String,
    val myRecipes : List<String>,
    val theirRecipes : List<String>,
)

data class NewChat (
    val id: String? = null,
    val userId1 : String? = null,
    val userId2 : String? = null,
)

data class Message (
    val id: String? = null,
    val content: String? = null,
    val timestamp: Long? = null,
    val fromUser: String? = null,
)

fun Message.toMessageDTO() : MessageDTO {

    return MessageDTO(
        id = this.id ?: statusException(HttpStatusCode.InternalServerError),
        senderId = this.fromUser ?: statusException(HttpStatusCode.InternalServerError),
        timestamp = this.timestamp ?: statusException(HttpStatusCode.InternalServerError),
        content = this.content ?: statusException(HttpStatusCode.InternalServerError),
    )
}
