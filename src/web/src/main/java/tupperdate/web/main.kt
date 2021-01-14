@file:JvmName("Main")

package tupperdate.web

import com.google.firebase.auth.FirebaseAuth
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.get
import tupperdate.web.facade.profiles.KoinModuleFacadeProfile
import tupperdate.web.legacy.auth.firebase
import tupperdate.web.legacy.routing.accounts.accounts
import tupperdate.web.legacy.routing.chats.chats
import tupperdate.web.legacy.routing.recipes.recipes
import tupperdate.web.legacy.util.getPort
import tupperdate.web.model.impl.firestore.KoinModuleModelFirebase
import tupperdate.web.model.profiles.firestore.KoinModuleModelUsersFirestore

@JvmName("main")
fun main() {
    // Retrieve the port
    val port = getPort()

    val server = embeddedServer(Netty, port = port) {
        install(Koin) {
            modules(KoinModuleModelFirebase)
            modules(KoinModuleFacadeProfile)
            modules(KoinModuleModelUsersFirestore)
        }
        installServer()
    }

    server.start(wait = true)
}

fun Application.installServer() {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        json()
    }

    install(Authentication) {
        firebase(FirebaseAuth.getInstance(get()))
    }

    install(StatusPages) {
        registerException()
    }

    install(Routing) {
        authenticate {
            endpoints()
            accounts(get())
            recipes(get())
            chats(get())
        }
    }
}
