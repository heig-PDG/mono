package tupperdate.web.facade.chats

import tupperdate.web.facade.recipes.Recipe

data class Chat(
    val userId: String,
    val displayName: String,
    val picture: String?,
    val lastMessage: Message?,
    val myRecipes: List<Recipe>,
    val theirRecipes: List<Recipe>,
)
