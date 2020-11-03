@file:JvmName("Main")

package tupperdate.web

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.io.FileInputStream
import java.nio.file.Paths


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

        val ref = FirebaseDatabase.getInstance()
            .getReference("test")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val document = dataSnapshot.value
                println(document)
            }

            override fun onCancelled(error: DatabaseError) {}
        })

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