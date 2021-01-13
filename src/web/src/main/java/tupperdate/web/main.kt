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
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.get
import org.koin.ktor.ext.inject
import tupperdate.web.facade.chats.KoinModuleFacadeChat
import tupperdate.web.facade.profiles.KoinModuleFacadeProfile
import tupperdate.web.facade.recipes.KoinModuleFacadeRecipe
import tupperdate.web.legacy.auth.firebase
import tupperdate.web.legacy.routing.accounts.accounts
import tupperdate.web.legacy.routing.chats.chats
import tupperdate.web.legacy.routing.recipes.recipes
import tupperdate.web.legacy.util.getPort
import tupperdate.web.legacy.util.initialiseApp
import tupperdate.web.model.chats.firestore.KoinModuleModelChatsFirestore
import tupperdate.web.model.impl.firestore.KoinModuleModelFirebase
import tupperdate.web.model.profiles.firestore.KoinModuleModelUsersFirestore
import tupperdate.web.model.recipes.firestore.KoinModuleModelRecipesFirestore
import java.io.ByteArrayInputStream
import java.io.InputStream

@JvmName("main")
fun main() {
    // Retrieve the port
    val port = getPort()

    val server = embeddedServer(Netty, port = port) {
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

    install(Koin) {
        modules(KoinModuleModelFirebase)

        modules(KoinModuleFacadeProfile)
        //modules(KoinModuleFacadeRecipe)
        //modules(KoinModuleFacadeChat)

        modules(KoinModuleModelUsersFirestore)
        //modules(KoinModuleModelRecipesFirestore)
        //modules(KoinModuleModelChatsFirestore)
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
