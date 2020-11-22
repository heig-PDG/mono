package tupperdate.web.routing

import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import tupperdate.common.model.User
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.util.await

fun Routing.users(firestore: Firestore) {
    route("/users") {
        val users = firestore.collection("users")

        /**
         * Get a user by id
         * @param userId as a [String] id in the endpoint path
         */
        get("{userId}") {
            val userId = call.parameters["userId"] ?: ""

            val user = users
                .document(userId)
                .get()
                .await()
                .toObject(User::class.java)

            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        /**
         * Post a new user given the request provides an authentication token
         */
        authenticate {
            post {
                val userId = call.firebaseAuthPrincipal?.uid ?: ""

                // Add auto-generated document to firestore
                val userDocument = users.document(userId)

                // Get post data
                val json = call.receiveText()

                // Parse JSON (and add auto-generated id to it)
                val user = Json.decodeFromString<User>(json).copy(id = userId)

                // Add user data to newly generated document
                userDocument.set(user)
            }
        }
    }
}
