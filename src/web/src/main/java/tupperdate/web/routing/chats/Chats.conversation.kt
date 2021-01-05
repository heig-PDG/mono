package tupperdate.web.routing.chats

import com.google.cloud.firestore.Firestore
import com.google.firebase.auth.FirebaseAuth
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.exceptions.statusException

fun Route.getConvs(store: Firestore) = get("/{userId}") {
    val uid = call.firebaseAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)
    
    call.respond(HttpStatusCode.OK)
}
