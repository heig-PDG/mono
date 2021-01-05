package tupperdate.web.routing.chats

import com.google.firebase.auth.FirebaseAuth
import io.ktor.routing.*

fun Route.chats(auth: FirebaseAuth) {
    route("/chats") {
        getChats(auth)
        getConvs(auth)
        getMessages(auth)
        postMessage(auth)
    }
}
