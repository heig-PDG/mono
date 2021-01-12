package tupperdate.web.legacy.routing.users

import com.google.cloud.firestore.Firestore
import com.google.firebase.auth.FirebaseAuth
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.common.dto.*
import tupperdate.web.legacy.auth.firebaseAuthPrincipal
import tupperdate.web.legacy.exceptions.*
import tupperdate.web.legacy.model.FirestoreUser
import tupperdate.web.legacy.model.toUserDTO
import tupperdate.web.legacy.util.await

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
        .get().await().toObject(FirestoreUser::class.java) ?: statusException(HttpStatusCode.NotFound)

        call.respond(HttpStatusCode.OK, user.toUserDTO(phone = phone))
}
