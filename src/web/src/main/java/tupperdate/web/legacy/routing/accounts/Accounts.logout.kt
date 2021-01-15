package tupperdate.web.legacy.routing.accounts

import com.google.firebase.auth.FirebaseAuth
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.web.utils.auth.tupperdateAuthPrincipal
import tupperdate.web.statusException

fun Route.logout(auth: FirebaseAuth) = post("logout") {
    val id = call.tupperdateAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)
    // TODO: This revokes all refresh tokens from every logged in device. Maybe do something else
    auth.revokeRefreshTokens(id.uid)

    call.respond(HttpStatusCode.OK)
}
