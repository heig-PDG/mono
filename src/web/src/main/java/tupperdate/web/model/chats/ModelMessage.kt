package tupperdate.web.model.chats

data class ModelMessage(
    val identifier: String,
    val content: String,
    val timestamp: Long,
    val senderId: String,
)
