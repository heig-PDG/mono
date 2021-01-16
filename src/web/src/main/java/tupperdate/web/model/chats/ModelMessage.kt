package tupperdate.web.model.chats

import tupperdate.web.facade.chats.NewMessage

data class ModelMessage(
    val identifier: String,
    val tempId: String,
    val content: String,
    val timestamp: Long,
    val senderId: String,
)

fun NewMessage.toModelNewMessage(): ModelNewMessage {
    return ModelNewMessage(
        tempId = tempId,
        content = content,
    )
}
