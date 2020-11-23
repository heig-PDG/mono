package tupperdate.web.routing

import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.common.dto.MyUserDTO
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.exceptions.BadRequestException
import tupperdate.web.exceptions.ForbiddenException
import tupperdate.web.exceptions.UnauthorizedException
import tupperdate.web.model.User
import tupperdate.web.model.toUser
import tupperdate.web.util.await

/**
 * Post a new user given an authentication token
 *
 * @param store the [Firestore] instance that is used.
 */
fun Route.usersPut(store: Firestore) = put("{userId}") {
    val pathUserId = call.parameters["userId"] ?: throw BadRequestException()
    val authUserId = call.firebaseAuthPrincipal?.uid ?: throw UnauthorizedException()
    if (authUserId != pathUserId) throw ForbiddenException()

    val doc = store.collection("users").document(authUserId)
    val dto = call.receive<MyUserDTO>()
    val user = dto.toUser(doc.id, "", "https://thispersondoesnotexist.com/image")

    doc.set(user).await()
    call.respond(HttpStatusCode.OK)
}
