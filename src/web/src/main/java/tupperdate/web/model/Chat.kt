package tupperdate.web.model

data class Chat (
    val id: String? = null,
    val userId1 : String? = null,
    val userId2 : String? = null,
    val user1LikedRecipes : List<String>? = null,
    val user2LikedRecipes : List<String>? = null,
)
