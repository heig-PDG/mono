package tupperdate.web.facade.chats

import tupperdate.common.dto.ConversationDTO
import tupperdate.common.dto.MessageDTO
import tupperdate.web.facade.recipes.Recipe
import tupperdate.web.facade.recipes.toRecipeDTO

data class Chat(
    val userId: String,
    val displayName: String,
    val picture: String?,
    val lastMessage: Message?,
    val myRecipes: List<Recipe>,
    val theirRecipes: List<Recipe>,
)

fun Chat.toConversationDTO(): ConversationDTO {
    return ConversationDTO(
        userId = userId,
        displayName = displayName,
        picture = picture,
        lastMessage = lastMessage?.let { MessageDTO(
            id = it.id,
            tempId = it.tempId,
            senderId = it.senderId,
            timestamp = it.timestamp,
            content = it.content,
        ) },
        myRecipes = myRecipes.map { it.toRecipeDTO() },
        theirRecipes = theirRecipes.map {it.toRecipeDTO() }
    )
}

fun emptyChat(): Chat {
    return Chat(
        userId = "",
        displayName = "",
        picture = null,
        lastMessage = Message(
            id = "",
            tempId = "",
            senderId = "",
            timestamp = 0,
            content = "",
        ),
        myRecipes = listOf(),
        theirRecipes = listOf(),
    )
}
