package tupperdate.android.data.features.notifications.impl

import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.notifications.Notification
import tupperdate.android.data.features.notifications.Notification.*
import tupperdate.common.dto.notifications.Notifications.KeyArgumentConversationId
import tupperdate.common.dto.notifications.Notifications.KeyArgumentRecipeId
import tupperdate.common.dto.notifications.Notifications.KeyType
import tupperdate.common.dto.notifications.Notifications.TypeSyncAllConversations
import tupperdate.common.dto.notifications.Notifications.TypeSyncAllOwnRecipes
import tupperdate.common.dto.notifications.Notifications.TypeSyncAllStackRecipes
import tupperdate.common.dto.notifications.Notifications.TypeSyncOneConversation
import tupperdate.common.dto.notifications.Notifications.TypeSyncOneRecipe
import tupperdate.common.dto.notifications.Notifications.TypeSyncProfile

/**
 * Transforms a [RemoteMessage] into a [Notification] that can be used on the client to perform
 * certain kinds of actions.
 *
 * If no corresponding action can be found, a `null` [Notification] will be returned instead.
 */
@InternalDataApi
fun RemoteMessage.toNotification(): Notification? {
    return when (this.data[KeyType]) {
        TypeSyncAllOwnRecipes -> SyncAllOwnRecipes
        TypeSyncAllStackRecipes -> SyncAllStackRecipes
        TypeSyncAllConversations -> SyncAllConversations
        TypeSyncProfile -> SyncProfile
        TypeSyncOneConversation -> when (val id = this.data[KeyArgumentConversationId]) {
            null -> null
            else -> SyncOneConversation(id)
        }
        TypeSyncOneRecipe -> when (val id = this.data[KeyArgumentRecipeId]) {
            null -> null
            else -> SyncOneRecipe(id)
        }
        else -> {
            Log.d("RemoteMessage", "Data ${this.data.entries.joinToString()} not recognized.")
            null
        }
    }
}
