package tupperdate.web.model.accounts

/**
 * A sealed class representing the different kinds of notification messages that might be generated
 * and sent from the server.
 */
sealed class Notification {

    // Messages sent to individual users.
    sealed class ToUser(val user: String): Notification() {
        class UserSyncAllOwnRecipes(recipient: String) : ToUser(recipient)
        class UserSyncAllStackRecipes(recipient: String) : ToUser(recipient)
        class UserSyncAllConversations(recipient: String) : ToUser(recipient)
        class UserSyncProfile(recipient: String) : ToUser(recipient)
        class UserSyncOneConversation(recipient: String, val id: String) : ToUser(recipient)
        class UserSyncOneRecipe(recipient: String, val id: String) : ToUser(recipient)
    }

    // Collapsible messages sent to all users.
    sealed class ToAll: Notification() {
        object UserSyncAllStackRecipes: ToAll()
    }
}
