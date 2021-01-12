package tupperdate.web.facade.chats

data class Message(
    val id: String,
    val senderId: String,
    val timestamp: Long,
    val content: String,
)
