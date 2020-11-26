package tupperdate.web.routing.users

import com.google.cloud.firestore.Firestore
import io.ktor.routing.*

fun Route.users(store: Firestore) {
    route("/users") {
        usersPut(store)
        usersGet(store)
    }
}
