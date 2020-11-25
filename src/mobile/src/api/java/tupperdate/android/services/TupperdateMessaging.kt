package tupperdate.android.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class TupperdateMessaging : FirebaseMessagingService() {

    override fun onNewToken(newToken: String) {
        // TODO : Send the token to the server.
    }

    override fun onMessageReceived(message: RemoteMessage) {
    }
}
