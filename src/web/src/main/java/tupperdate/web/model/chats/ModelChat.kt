package tupperdate.web.model.chats

data class ModelChat(
    val identifier: String,
    val user1: String,
    val user2: String,
    val user1Recipes: List<String>?,
    val user2Recipes: List<String>?,
)
