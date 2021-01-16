package tupperdate.android.data.features.notifications

import tupperdate.android.data.features.auth.firebase.FirebaseUid

/**
 * A sealed class representing the different kinds of notification messages that might be received
 * from the server.
 *
 * Different actions might be performed on the client depending on the notification contents.
 */
sealed class Notification {
    // Sync groups of objects.
    object SyncAllOwnRecipes : Notification()
    object SyncAllStackRecipes : Notification()
    object SyncAllConversations : Notification()

    // Sync a single kind of object.
    object SyncProfile : Notification()
    data class SyncOneConversation(val id: FirebaseUid) : Notification()
    data class SyncOneRecipe(val id: String) : Notification()
}
