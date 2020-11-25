@file:JvmName("Main")

package tupperdate.web

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.cloud.FirestoreClient
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import tupperdate.web.auth.firebase
import tupperdate.web.exceptions.registerException
import tupperdate.web.routing.recipes
import tupperdate.web.routing.users

private const val DefaultPort = 1234
private const val DefaultPortEnvVariable = "PORT"
private const val DefaultGoogleServiceAccountEnvVariable = "GOOGLE_SERVICE_ACCOUNT"
private const val DefaultGoogleDatabaseName = "GOOGLE_DATABASE_NAME"
private const val DefaultGoogleBucketName = "GOOGLE_BUCKET_NAME"

/**
 * Retrieves the service account from the environment variables and returns the associated
 * [GoogleCredentials]. If no credential could be built, and exception will be thrown.
 */
private fun retrieveServiceAccount(): GoogleCredentials {
    return requireNotNull(
        System.getenv(DefaultGoogleServiceAccountEnvVariable)
            .byteInputStream()
            .let(GoogleCredentials::fromStream)
    ) { "Missing \$$DefaultGoogleServiceAccountEnvVariable environment variable." }
}

@JvmName("main")
fun main() {
    val port = System.getenv(DefaultPortEnvVariable)?.toIntOrNull() ?: DefaultPort
    val database = requireNotNull(System.getenv(DefaultGoogleDatabaseName)) {
        "Missing \$$DefaultGoogleDatabaseName environment variable."
    }
    val storage = requireNotNull(System.getenv(DefaultGoogleBucketName)) {
        "Missing \$$DefaultGoogleBucketName environment variable."
    }

    val options = FirebaseOptions.builder()
        .setCredentials(retrieveServiceAccount())
        .setDatabaseUrl(database)
        .setStorageBucket(storage)
        .build()

    // Initialise FirebaseApp
    val firebase = FirebaseApp.initializeApp(options)

    val server = embeddedServer(Netty, port = port) {
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
                recipes(firebase)
                users(FirestoreClient.getFirestore(firebase))
            }
        }
    }

    server.start(wait = true)
}
