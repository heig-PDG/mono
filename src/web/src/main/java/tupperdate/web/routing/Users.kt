package tupperdate.web.routing

import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tupperdate.common.dto.MyUserDTO
import tupperdate.common.dto.UserDTO
import tupperdate.common.model.User
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.exceptions.ForbiddenException
import tupperdate.web.exceptions.UnauthorizedException
import tupperdate.web.util.await

fun Route.users(firestore: Firestore) {
    route("/users") {
        val users = firestore.collection("users")

        /**
         * Post a new user given an authentication token
         * @authenticated
         */
        put("{userId}") {
            try {
                val pathUserId = call.parameters["userId"]
                    ?: throw BadRequestException("userId missing in path")
                val authUserId = call.firebaseAuthPrincipal?.uid
                    ?: throw UnauthorizedException()

                if (authUserId != pathUserId) {
                    throw ForbiddenException("an authenticated user can't modify another user")
                }

                // Add auto-generated document to firestore
                val userDocument = users.document(authUserId)

                // Get post data
                val json = call.receiveText()

                // Parse JSON (and add auto-generated id to it)
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

        /**
         * Get a user by id
         * @authenticated
         */
        get("{userId}") {
            try {
                val pathUserId = call.parameters["userId"]
                    ?: throw BadRequestException("userId missing in path")
                val authUserId = call.firebaseAuthPrincipal?.uid
                    ?: throw UnauthorizedException()

                // TODO: Decide what happens if an authenticated user gets another user
                val user = users
                    .document(pathUserId)
                    .get()
                    .await()
                    .toObject(UserDTO::class.java)

                if (user != null) {
                    call.respond(HttpStatusCode.OK, Json.encodeToString(user))
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            } catch (exception: BadRequestException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (exception: UnauthorizedException) {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}
