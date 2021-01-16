package tupperdate.web.model.accounts.fcm

import com.google.firebase.messaging.Message
import tupperdate.common.dto.notifications.Notifications
import tupperdate.common.dto.notifications.Notifications.KeyArgumentConversationId
import tupperdate.common.dto.notifications.Notifications.KeyArgumentRecipeId
import tupperdate.common.dto.notifications.Notifications.KeyType
import tupperdate.common.dto.notifications.Notifications.TypeSyncAllConversations
import tupperdate.common.dto.notifications.Notifications.TypeSyncAllOwnRecipes
import tupperdate.common.dto.notifications.Notifications.TypeSyncAllStackRecipes
import tupperdate.common.dto.notifications.Notifications.TypeSyncOneConversation
import tupperdate.common.dto.notifications.Notifications.TypeSyncOneRecipe
import tupperdate.common.dto.notifications.Notifications.TypeSyncProfile
import tupperdate.web.model.accounts.Notification
import tupperdate.web.model.accounts.Notification.ToAll
import tupperdate.web.model.accounts.Notification.ToUser
import tupperdate.web.model.accounts.Notification.ToUser.*

suspend fun Notification.toMessage(
    getToken: suspend (String) -> String,
): Message = when (this) {
    is ToUser -> when (this) {

        is UserSyncAllOwnRecipes -> Message.builder()
            .setToken(getToken(this.user))
            .putData(KeyType, TypeSyncAllOwnRecipes)
            .build()

        is UserSyncAllStackRecipes -> Message.builder()
            .setToken(getToken(this.user))
            .putData(KeyType, TypeSyncAllStackRecipes)
            .build()

        is UserSyncAllConversations -> Message.builder()
            .setToken(getToken(this.user))
            .putData(KeyType, TypeSyncAllConversations)
            .build()

        is UserSyncProfile -> Message.builder()
            .setToken(getToken(this.user))
            .putData(KeyType, TypeSyncProfile)
            .build()

        is UserSyncOneConversation -> Message.builder()
            .setToken(getToken(this.user))
            .putData(KeyType, TypeSyncOneConversation)
            .putData(KeyArgumentConversationId, this.id)
            .build()

        is UserSyncOneRecipe -> Message.builder()
            .setToken(getToken(this.user))
            .putData(KeyType, TypeSyncOneRecipe)
            .putData(KeyArgumentRecipeId, this.id)
            .build()
    }

    is ToAll -> when (this) {

        ToAll.UserSyncAllStackRecipes -> Message.builder()
            .setTopic(Notifications.TopicStack)
            .putData(KeyType, TypeSyncAllStackRecipes)
            .build()
    }
}
