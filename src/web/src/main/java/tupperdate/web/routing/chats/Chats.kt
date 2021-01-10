package tupperdate.web.routing.chats

import com.google.cloud.firestore.Firestore
import com.google.firebase.auth.FirebaseAuth
import io.ktor.routing.*

fun Route.chats(store: Firestore) {
    route("/chats") {
        getChats(store)
        getConvs(store)
        getMessages(store)
        postMessage(store)
    }
}
