@file:JvmName("Main")

package tupperdate.web

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import tupperdate.web.auth.firebase
import tupperdate.web.exceptions.registerException
import tupperdate.web.routing.accounts.accounts
import tupperdate.web.routing.recipes.recipes
import tupperdate.web.routing.users.users
import tupperdate.web.util.*

@JvmName("main")
fun main() {
    // Retrieve the port
    val port = getPort()

    // Initialise FirebaseApp
    val firebase = initialiseApp()

    val server = embeddedServer(Netty, port = port) {
        installServer(firebase)
    }

    server.start(wait = true)
}

fun Application.installServer(firebase: FirebaseApp) {
    install(Authentication) {
        firebase(FirebaseAuth.getInstance(firebase))
    }
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        json()
    }

    install(StatusPages) {
        registerException()
    }

    install(Routing) {
        authenticate {
            accounts(FirebaseAuth.getInstance(firebase))
            recipes(firebase)
            users(firebase, FirebaseAuth.getInstance(firebase))
        }
    }
}
