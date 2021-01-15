package tupperdate.common.dto.notifications

/**
 * An object with the different keys and values that might be contained in cloud messages. These
 * constants are shared between the client and the server to ensure convergence.
 */
object Notifications {
    // Key types.

    // All messages have a KeyType.
    const val KeyType = "tupperdate_type"

    // Only for TypeSyncOneConversation messages.
    const val KeyArgumentConversationId = "tupperdate_arg_conversation"

    // Only for TypeSyncOneRecipe messages.
    const val KeyArgumentRecipeId = "tupperdate_arg_recipe"

    // Messages with zero arguments.
    const val TypeSyncAllOwnRecipes = "tupperdate_act_sync_own_recipes"
    const val TypeSyncAllStackRecipes = "tupperdate_act_sync_stack_recipes"
    const val TypeSyncAllConversations = "tupperdate_act_sync_all_conversations"
    const val TypeSyncProfile = "tupperdate_act_sync_profile"
    const val TypeSyncOneConversation = "tupperdate_act_sync_one_conversation"
    const val TypeSyncOneRecipe = "tupperdate_act_sync_one_recipe"
}
