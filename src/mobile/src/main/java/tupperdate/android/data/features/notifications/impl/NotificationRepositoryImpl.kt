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
        when (notification) {
            Notification.SyncAllOwnRecipes -> manager.enqueue(
                SyncRequestBuilder<RefreshOwnWorker>()
                    .build()
            )
            Notification.SyncAllStackRecipes -> manager.enqueue(
                SyncRequestBuilder<RefreshStackWorker>()
                    .build()
            )
            Notification.SyncAllConversations -> manager.enqueue(
                SyncRequestBuilder<RefreshAllConversationsWorker>()
                    .build()
            )
            Notification.SyncProfile -> manager.enqueue(
                SyncRequestBuilder<RefreshProfileWorker>()
                    .build()
            )
            is Notification.SyncOneConversation -> manager.enqueue(
                SyncRequestBuilder<RefreshMessagesWorker>()
                    .setInputData(RefreshMessagesWorker.forConversation(notification.id))
                    .build()
            )
            is Notification.SyncOneRecipe -> Unit // TODO : Handle this case.
        }
    }
}
