package tupperdate.web.legacy.routing.users

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.cloud.FirestoreClient
import io.ktor.routing.*

fun Route.users(firebase: FirebaseApp, auth: FirebaseAuth) {
    route("/users") {
        usersPut(firebase)
        usersGet(FirestoreClient.getFirestore(firebase), auth)
    }
}
