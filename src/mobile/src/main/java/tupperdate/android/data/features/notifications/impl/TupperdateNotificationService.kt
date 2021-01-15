package tupperdate.android.data.features.notifications.impl

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.android.ext.android.inject
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.notifications.NotificationRepository
import tupperdate.android.data.features.notifications.NotificationToken

/**
 * An implementation of a [FirebaseMessagingService] that will be triggered whenever a new token
 * for FCM is associated to this app instance.
 */
@InternalDataApi
class TupperdateNotificationService : FirebaseMessagingService() {

    private val repo by inject<NotificationRepository>()

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // Send the token to the server.
        repo.link(NotificationToken(token))
    }

    override fun onMessageReceived(remote: RemoteMessage) {
        super.onMessageReceived(remote)

        Log.d("NotificationService", "Got remote message")

        // Send the notification.
        remote.toNotification()?.let(repo::handle)
    }
}
