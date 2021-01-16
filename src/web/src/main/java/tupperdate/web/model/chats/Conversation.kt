package tupperdate.web.model.chats

data class Conversation(
    val id: String,
    val myId: String,
    val theirId: String,
    val myRecipes: List<String>,
    val theirRecipes: List<String>,
)
