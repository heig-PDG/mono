package tupperdate.web.facade.chats

import tupperdate.common.dto.MessageDTO
import tupperdate.web.model.chats.ModelMessage

data class Message(
    val id: String,
    val tempId: String,
    val senderId: String,
    val timestamp: Long,
    val content: String,
)

fun ModelMessage.toMessage(): Message {
    return Message(
        id = identifier,
        tempId = tempId,
        senderId = senderId,
        timestamp = timestamp,
        content = content,
    )
}

fun Message.toMessageDTO(): MessageDTO {
    return MessageDTO(
        id = id,
        tempId = tempId,
        senderId = senderId,
        timestamp = timestamp,
        content = content,
    )
}
