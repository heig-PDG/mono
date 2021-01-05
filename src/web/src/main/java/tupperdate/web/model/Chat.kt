package tupperdate.web.model

data class Chat (
    val id: String? = null,
    val userId1 : String? = null,
    val userId2 : String? = null,
    val user1LikedRecipes : List<String>? = null,
    val user2LikedRecipes : List<String>? = null,
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
