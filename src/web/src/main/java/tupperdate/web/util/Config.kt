package tupperdate.web.util

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

private const val DefaultPort = 1234
private const val DefaultPortEnvVariable = "PORT"
private const val DefaultGoogleServiceAccountEnvVariable = "GOOGLE_SERVICE_ACCOUNT"
private const val DefaultGoogleDatabaseName = "GOOGLE_DATABASE_NAME"
private const val DefaultGoogleBucketName = "GOOGLE_BUCKET_NAME"

/**
 * Retrieves the service account from the environment variables and returns the associated
 * [GoogleCredentials]. If no credential could be built, and exception will be thrown.
 */
private fun getServiceAccount(): GoogleCredentials = requireNotNull(
            System.getenv(DefaultGoogleServiceAccountEnvVariable)
                    .byteInputStream()
                    .let(GoogleCredentials::fromStream)
    ) { "Missing \$$DefaultGoogleServiceAccountEnvVariable environment variable." }

/**
 * Retrieves the database name from the environment variables and returns it. If no database name
 * could be parsed, and exception will be thrown.
 */
private fun getDatabase(): String = requireNotNull(System.getenv(DefaultGoogleDatabaseName)) {
    "Missing \$$DefaultGoogleDatabaseName environment variable."
}

/**
 * Retrieves the storage url (aka bucket name) from the environment variables and returns it.
 * If no storage url could be parsed, and exception will be thrown.
 */
private fun getStorage(): String = requireNotNull(System.getenv(DefaultGoogleBucketName)) {
        "Missing \$$DefaultGoogleBucketName environment variable."
    }.apply {
        if (this != "tupperdate-developement.appspot.com") {
            throw RuntimeException("Wrong bucket name ($this)")
        }
    }

/**
 * Builds the [FirebaseOptions] object from the environment variables and returns it.
 */
private fun getOptions(): FirebaseOptions = FirebaseOptions.builder()
    .setStorageBucket(getStorage())
    .setCredentials(getServiceAccount())
    .setDatabaseUrl(getDatabase())
    .build()

/**
 * Retrieves the port from the environment variables and returns it. If no port could be parsed,
 * the [DefaultPort] variable will be returned.
 */
fun getPort(): Int = System.getenv(DefaultPortEnvVariable)?.toIntOrNull() ?: DefaultPort

/**
 * Initialized the FirebaseApp from the environment variables and returns it.
 */
fun initialiseApp(): FirebaseApp = FirebaseApp.initializeApp(getOptions())
