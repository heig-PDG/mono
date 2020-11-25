package tupperdate.web.routing

import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Firestore
import com.google.firebase.messaging.FirebaseMessaging
import io.ktor.application.*
import io.ktor.http.HttpStatusCode.Companion.Forbidden
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.request.*
import io.ktor.routing.*
import tupperdate.common.dto.NotificationTokenDTO
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.exceptions.statusException
import tupperdate.web.model.User
import tupperdate.web.util.await

/**
 * Handles FCM-related API calls that register and unregister devices from specific topics.
 */
fun Route.notifications(
    messaging: FirebaseMessaging,
    firestore: Firestore,
) {
    route("/notifications") {
        handleRegister(messaging, firestore)
        handleUnregister(messaging, firestore)
    }
}

private fun Route.handleRegister(
    messaging: FirebaseMessaging,
    firestore: Firestore,
) {
    put("/register") {

        // HTTP request information.
        val uid = call.firebaseAuthPrincipal?.uid ?: statusException(Forbidden)
        val data = call.receive<NotificationTokenDTO>()

        // Update the User document.
        firestore.runTransaction { transaction ->
            val ref = firestore.collection("users").document(uid)
            val document: DocumentSnapshot? = transaction.get(ref).get()
            val updated = if (document == null) {
                User(id = uid, fcmTokens = listOf(data.token))
            } else {
                val user = document.toObject(User::class.java)
                    ?: statusException(InternalServerError)
                // Add de-duplicated token.
                user.copy(fcmTokens = user.fcmTokens - data.token + data.token)
            }
            transaction.set(ref, updated)
        }.await()

        // Register to the appropriate topics.
        messaging.subscribeToTopic(listOf(data.token), "recipes")
    }
}

private fun Route.handleUnregister(
    messaging: FirebaseMessaging,
    firestore: Firestore,
) {
    put("/unregister") {

        // HTTP request information.
        val uid = call.firebaseAuthPrincipal?.uid ?: statusException(Forbidden)
        val data = call.receive<NotificationTokenDTO>()

        // Update the User document.
        firestore.runTransaction { transaction ->
            val ref = firestore.collection("users").document(uid)
            val document: DocumentSnapshot? = transaction.get(ref).get()
            val updated = if (document == null) {
                User(id = uid, fcmTokens = listOf(data.token))
            } else {
                val user = document.toObject(User::class.java)
                    ?: statusException(InternalServerError)
                // Remove the token.
                user.copy(fcmTokens = user.fcmTokens - data.token)
            }
            transaction.set(ref, updated)
        }.await()

        // Register to the appropriate topics.
        messaging.unsubscribeFromTopic(listOf(data.token), "recipes")
    }
}
