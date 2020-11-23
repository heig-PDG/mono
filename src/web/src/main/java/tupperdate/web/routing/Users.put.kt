package tupperdate.web.routing

import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.common.dto.MyUserDTO
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.exceptions.statusException
import tupperdate.web.model.toUser
import tupperdate.web.util.await

/**
 * Post a new [MyUserDTO]
 *
 * @param store the [Firestore] instance that is used.
 */
fun Route.usersPut(store: Firestore) = put("{userId}") {
    val pathUserId = call.parameters["userId"] ?: statusException(HttpStatusCode.BadRequest)
    val authUserId = call.firebaseAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)
    if (authUserId != pathUserId) throw statusException(HttpStatusCode.Forbidden)

    val doc = store.collection("users").document(authUserId)
    val dto = call.receive<MyUserDTO>()
    val user = dto.toUser(doc.id, "", "https://thispersondoesnotexist.com/image")

    doc.set(user).await()
    call.respond(HttpStatusCode.OK)
}
