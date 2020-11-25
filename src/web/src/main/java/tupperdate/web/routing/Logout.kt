package tupperdate.web.routing

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.exceptions.statusException

/**
 *
 */

fun Route.account(auth: FirebaseAuth) {
    route("/account") {
        logout(auth)
    }
}

fun Route.logout(auth: FirebaseAuth) = post("logout") {
    val uid = call.firebaseAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)
    auth.revokeRefreshTokens(uid)

    call.respond(HttpStatusCode.OK)
}
