package tupperdate.web.routing.users

import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.cloud.FirestoreClient
import io.ktor.routing.*

fun Route.users(firebase: FirebaseApp) {
    route("/users") {
        usersPut(firebase)
        usersGet(FirestoreClient.getFirestore(firebase))
    }
}
