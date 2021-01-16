package tupperdate.web.model.accounts.fcm

import com.google.cloud.firestore.FieldMask
import com.google.cloud.firestore.Firestore
import com.google.firebase.messaging.FirebaseMessaging
import tupperdate.web.utils.await
import tupperdate.web.model.Result
import tupperdate.web.model.accounts.Notification
import tupperdate.web.model.accounts.NotificationRepository

class FcmNotificationRepository(
    private val messaging: FirebaseMessaging,
    private val store: Firestore,
) : NotificationRepository {

    private suspend fun tokenFor(uid: String): String {
        val document = store.collection("users")
            .document(uid)
            .get(FieldMask.of("notifications"))
            .await()
        return document["notifications"] as String
    }

    override suspend fun send(
        notification: Notification,
    ): Result<Unit> = try {
        val message = notification.toMessage(
            getToken = this::tokenFor,
        )
        messaging.sendAsync(message).await()
        Result.Ok(Unit)
    } catch (throwable: Throwable) {
        Result.MissingData()
    }
}
