package tupperdate.web.routing

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.exceptions.statusException

fun Route.accounts(auth: FirebaseAuth) {
    route("/accounts") {
        logout(auth)
    }
}
