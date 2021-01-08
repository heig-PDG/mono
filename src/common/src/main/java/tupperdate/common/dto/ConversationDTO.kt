package tupperdate.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class ConversationDTO(
    val userId: String,
    val displayName: String,
    val picture: String,
    val lastMessage: MessageDTO?,
    val myRecipes: List<RecipeDTO>,
    val theirRecipes: List<RecipeDTO>,
)
