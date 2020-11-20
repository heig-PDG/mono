package tupperdate.web.routing

import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import tupperdate.common.model.User
import tupperdate.web.await

fun Routing.users(firestore: Firestore) {
    route("/users") {
        val users = firestore.collection("users")

        post {
            // Add auto-generated document to firestore
            val userDocument = users.document()

            // Get post data
            val json = call.receiveText()

            // Parse JSON (and add auto-generated id to it)
            val user = Json.decodeFromString<User>(json).copy(id = userDocument.id)

            // Add user data to newly generated document
            userDocument.set(user)
        }

        get("{userId}") {
            val userId = call.parameters["userId"] ?: "Error"
            val userDocument = users.document(userId)
            call.respondText(userDocument.get().await().data.toString())
        }
    }
}
