package tupperdate.web.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import tupperdate.web.auth.FirebaseAuthenticationProvider.Configuration

/**
 * A [FirebaseAuthPrincipal] represents some information about the currently authenticated
 * Firebase user.
 */
data class FirebaseAuthPrincipal(
    val uid: String
) : Principal

/**
 * An [AuthenticationProvider] for the Firebase Auth SDK.
 *
 * @param configuration the [Configuration] to use for firebase authentication.
 */
class FirebaseAuthenticationProvider(
    configuration: Configuration,
) : AuthenticationProvider(configuration) {

    internal val authenticationFunction = configuration.authenticationFunction

    /**
     * A [Configuration] for Firebase Authentication.
     */
    class Configuration internal constructor(
        auth: FirebaseAuth,
        name: String?
    ) : AuthenticationProvider.Configuration(name) {
        internal var authenticationFunction: AuthenticationFunction<String> = { token ->
            try {
                // Check if id token is valid and was issued before refresh tokens were revoked
                val checked = auth.verifyIdToken(token, true)
                FirebaseAuthPrincipal(checked.uid)
            } catch (argEx: IllegalArgumentException) {
                null
            } catch (authEx: FirebaseAuthException) {
                null
            }
        }
    }
}

/**
 * An [Authentication] feature that provides authentication with the Firebase Admin SDK, and
 * indicates failures if the user is not properly authenticated.
 *
 * @param auth the [FirebaseAuth] to use for authentication.
 * @param configure a [Configuration] scope.
 */
fun Authentication.Configuration.firebase(
    auth: FirebaseAuth,
    configure: Configuration.() -> Unit = {},
) {
    val provider = FirebaseAuthenticationProvider(Configuration(auth, null).apply(configure))
    val authenticate = provider.authenticationFunction

    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->
        val token = call.request.firebaseToken()
        val userId = token?.let { authenticate(call, it) }

        val failure = when {
            token == null -> AuthenticationFailedCause.NoCredentials
            userId == null -> AuthenticationFailedCause.InvalidCredentials
            else -> null
        }

        if (failure != null) {
            context.challenge(FirebaseAuthChallengeKey, failure) {
                call.respond(UnauthorizedResponse())
                it.complete()
            }
        }
        if (userId != null) {
            context.principal(userId)
        }
    }

    register(provider)
}

/**
 * Extracts the firebase authentication token from the [ApplicationRequest].
 */
private fun ApplicationRequest.firebaseToken(): String? {
    val data = header("Authorization") ?: return null
    if (!data.startsWith("Bearer")) return null
    return data.replaceFirst("Bearer", "").trim()
}

/**
 * Returns the [FirebaseAuthPrincipal] that is associated with the currently logged in user.
 *
 * TODO : Maybe we do not want to use this, if we offer additional authentication schemes.
 */
val ApplicationCall.firebaseAuthPrincipal: FirebaseAuthPrincipal?
    get() = this.authentication.principal()

// Challenge identification specific to firestore.
private const val FirebaseAuthChallengeKey = "FirebaseAuth"
