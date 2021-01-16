package tupperdate.android.data.features.notifications.impl

import android.util.Log
import androidx.work.WorkManager
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.SyncRequestBuilder
import tupperdate.android.data.features.auth.work.RefreshProfileWorker
import tupperdate.android.data.features.messages.work.RefreshAllConversationsWorker
import tupperdate.android.data.features.messages.work.RefreshMessagesWorker
import tupperdate.android.data.features.notifications.Notification
import tupperdate.android.data.features.notifications.NotificationRepository
import tupperdate.android.data.features.notifications.NotificationToken
import tupperdate.android.data.features.notifications.work.LinkWorker
import tupperdate.android.data.features.recipe.work.RefreshOwnWorker
import tupperdate.android.data.features.recipe.work.RefreshStackWorker
import tupperdate.android.data.features.recipe.work.TryOnceRefreshRecipeWorker

@InternalDataApi
class NotificationRepositoryImpl(
    private val manager: WorkManager,
) : NotificationRepository {

    override fun link(
        token: NotificationToken,
    ) {
        val request = SyncRequestBuilder<LinkWorker>()
            .setInputData(LinkWorker.forToken(token.value))
            .build()

        manager.enqueue(request)
    }

    override fun handle(
        notification: Notification,
    ) {
        Log.d("NotificationRepository", "Got $notification")

        val request = when (notification) {
            Notification.SyncAllOwnRecipes -> SyncRequestBuilder<RefreshOwnWorker>().build()
            Notification.SyncAllStackRecipes -> SyncRequestBuilder<RefreshStackWorker>().build()
            Notification.SyncAllConversations -> SyncRequestBuilder<RefreshAllConversationsWorker>().build()
            Notification.SyncProfile -> SyncRequestBuilder<RefreshProfileWorker>().build()
            is Notification.SyncOneConversation -> SyncRequestBuilder<RefreshMessagesWorker>().setInputData(
                RefreshMessagesWorker.forConversation(notification.id)
            ).build()
            is Notification.SyncOneRecipe -> SyncRequestBuilder<TryOnceRefreshRecipeWorker>().setInputData(
                TryOnceRefreshRecipeWorker.forId(notification.id)
            ).build()
        }

        manager.enqueue(request)
    }
}
