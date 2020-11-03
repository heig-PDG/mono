@file:JvmName("Main")

package tupperdate.web

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.io.FileInputStream

private const val DefaultPort = 1234
private const val DefaultPortEnvVariable = "PORT"

@JvmName("main")
fun main() {
    val port = System.getenv(DefaultPortEnvVariable)?.toIntOrNull() ?: DefaultPort

    val server = embeddedServer(Netty, port = port) {
        // Firebase options
        val serviceAccount = FileInputStream("web\\tupperdate-developement-serice-account-firebase-admin-key.json")

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://tupperdate-developement.firebaseio.com")
            .build()

        // Initialise FirebaseApp
        FirebaseApp.initializeApp(options)

        val firestore = FirestoreClient.getFirestore()
        val testCollection = firestore.collection("test")
        val data = hashMapOf(
            "hello" to "there",
            "general" to "kenobi"
        )
        testCollection.document("greetings").set(data as Map<String, Any>)

        install(DefaultHeaders)
        install(CallLogging)
        install(Routing) {
            get("/") {
                call.respondText("Hello world")
            }
        }
    }

    server.start(wait = true)
}