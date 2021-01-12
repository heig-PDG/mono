@file:JvmName("Main")

package tupperdate.web

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.cloud.FirestoreClient
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.Koin
import tupperdate.web.facade.chats.KoinModuleFacadeChat
import tupperdate.web.facade.profiles.KoinModuleFacadeProfile
import tupperdate.web.facade.recipes.KoinModuleFacadeRecipe
import tupperdate.web.legacy.auth.firebase
import tupperdate.web.legacy.exceptions.registerException
import tupperdate.web.legacy.routing.accounts.accounts
import tupperdate.web.legacy.routing.chats.chats
import tupperdate.web.legacy.routing.recipes.recipes
import tupperdate.web.legacy.routing.users.users
import tupperdate.web.legacy.util.getPort
import tupperdate.web.legacy.util.initialiseApp
import tupperdate.web.model.chats.firestore.KoinModuleModelChatsFirestore
import tupperdate.web.model.impl.firestore.KoinModuleModelFirestore
import tupperdate.web.model.profiles.firestore.KoinModuleModelUsersFirestore
import tupperdate.web.model.recipes.firestore.KoinModuleModelRecipesFirestore

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

    install(Koin) {
        modules(KoinModuleModelFirestore)

        modules(KoinModuleFacadeProfile)
        modules(KoinModuleFacadeRecipe)
        modules(KoinModuleFacadeChat)

        modules(KoinModuleModelFirestore)
        modules(KoinModuleModelUsersFirestore)
        modules(KoinModuleModelRecipesFirestore)
        modules(KoinModuleModelChatsFirestore)
    }

    install(StatusPages) {
        registerException()
    }

    install(Routing) {
        authenticate {
            accounts(FirebaseAuth.getInstance(firebase))
            recipes(firebase)
            users(firebase, FirebaseAuth.getInstance(firebase))
            chats(FirestoreClient.getFirestore(firebase))
        }
    }
}
