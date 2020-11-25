package tupperdate.web.notification

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message

fun main() {
    val messaging = FirebaseMessaging.getInstance()

    messaging.send(
        Message.builder()
            //.setTopic()
            .build()
    )
}
