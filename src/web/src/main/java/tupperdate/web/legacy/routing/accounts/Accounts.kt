package tupperdate.web.legacy.routing.accounts

import com.google.firebase.auth.FirebaseAuth
import io.ktor.routing.*

fun Route.accounts(auth: FirebaseAuth) {
    route("/accounts") {
        logout(auth)
    }
}
