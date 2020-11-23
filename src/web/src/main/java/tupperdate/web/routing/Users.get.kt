package tupperdate.web.routing

import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tupperdate.common.dto.*
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.exceptions.*
import tupperdate.web.util.await

/**
 * Get a user by id
 *
 * @param store the [Firestore] instance that is used.
 */
fun Route.usersGet(store: Firestore) = get("{userId}") {
    val pathUserId = call.parameters["userId"] ?: throw BadRequestException()
    val authUserId = call.firebaseAuthPrincipal?.uid ?: throw UnauthorizedException()

    // TODO: Decide what happens if an authenticated user gets another user
    val user = store.collection("users")
        .document(pathUserId)
        .get()
        .await()
        .toObject(UserDTO::class.java)

    if (user != null) {
        call.respond(HttpStatusCode.OK, Json.encodeToString(user))
    } else {
        call.respond(HttpStatusCode.NotFound)
    }
}
