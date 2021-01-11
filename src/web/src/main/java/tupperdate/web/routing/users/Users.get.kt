package tupperdate.web.routing.users

import com.google.cloud.firestore.Firestore
import com.google.firebase.auth.FirebaseAuth
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.common.dto.*
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.exceptions.*
import tupperdate.web.model.User
import tupperdate.web.model.toUserDTO
import tupperdate.web.util.await

/**
 * Retrieves a [UserDTO] by user id
 *
 * @param store the [Firestore] instance that is used.
 */
fun Route.usersGet(store: Firestore, auth: FirebaseAuth) = get("{userId}") {
    val uid = call.parameters["userId"] ?: statusException(HttpStatusCode.BadRequest)
    val authId = call.firebaseAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)
    val phone = auth.getUser(uid).phoneNumber

    if (authId != uid) statusException(HttpStatusCode.Unauthorized)

    val user = store.collection("users").document(uid)
        .get().await().toObject(User::class.java) ?: statusException(HttpStatusCode.NotFound)

        call.respond(HttpStatusCode.OK, user.toUserDTO(phone = phone))
}
