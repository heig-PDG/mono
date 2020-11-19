package tupperdate.web.routing

import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.web.await

fun Routing.users(firestore: Firestore) {
    route("/users") {
        val users = firestore.collection("users/")

        get {
            call.respondText(users.get().await().map { doc -> doc.id }.toString())
        }

        get("recipes") {
            call.respondText(users.get().await().map { doc -> doc.data.getValue("") }.toString())
        }

        route("{userId}") {
            get {
                val userId = call.parameters["userId"] ?: "Error"

                call.respondText {
                    users.document(userId).get().await().data.toString()
                }
            }

            route("recipes") {
                get("{recipeId}") {

                }
            }
        }
    }
}
