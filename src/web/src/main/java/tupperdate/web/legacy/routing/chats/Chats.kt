package tupperdate.web.legacy.routing.chats

import com.google.cloud.firestore.Firestore
import io.ktor.routing.*

fun Route.chats(store: Firestore) {
    route("/chats") {
        getConvs(store)
        getMessages(store)
        postMessage(store)
    }
}
