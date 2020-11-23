package tupperdate.web.routing

import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import tupperdate.common.dto.MyUserDTO
import tupperdate.common.model.User
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.exceptions.*

/**
 * Post a new user given an authentication token
 *
 * @param store the [Firestore] instance that is used.
 */
fun Route.usersPut(store: Firestore) = put("{userId}") {
    try {
        val users = store.collection("users")

        val pathUserId = call.parameters["userId"] ?: throw BadRequestException()
        val authUserId = call.firebaseAuthPrincipal?.uid ?: throw UnauthorizedException()
        if (authUserId != pathUserId) throw ForbiddenException()

        val userDocument = users.document(authUserId)

        val json = call.receiveText()

        val myUserDTO = Json.decodeFromString<MyUserDTO>(json)

        val user = User(
            id = authUserId,
            displayName = myUserDTO.displayName,
        )
        // Add user data to newly generated document
        userDocument.set(user)

        call.respond(HttpStatusCode.OK)

    } catch (exception: UnauthorizedException) {
        call.respond(HttpStatusCode.Unauthorized)
    } catch (exception: ForbiddenException) {
        call.respond(HttpStatusCode.Forbidden)
    }
}
