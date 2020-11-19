package tupperdate.web.routing

import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import tupperdate.common.model.User

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
    }
}
