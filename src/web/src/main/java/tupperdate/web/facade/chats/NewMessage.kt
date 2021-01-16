package tupperdate.web.facade.chats

import tupperdate.common.dto.MessageContentDTO

data class NewMessage(
    val content: String,
    val tempId: String,
)

fun MessageContentDTO.toNewMessage(): NewMessage {
    return NewMessage(
        content = content,
        tempId = tempId,
    )
}
