package tupperdate.web.legacy.auth

import com.google.firebase.auth.FirebaseAuthException
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import tupperdate.web.facade.accounts.AccountFacade
import tupperdate.web.facade.accounts.AccountId
import tupperdate.web.facade.accounts.Token
import tupperdate.web.legacy.auth.TupperdateAuthenticationProvider.Configuration
import tupperdate.web.model.Result

/**
 * A [TupperdateAuthPrincipal] represents some information about the currently authenticated
 * Firebase user.
 */
data class TupperdateAuthPrincipal(
    val uid: AccountId
) : Principal

/**
 * An [AuthenticationProvider] for the Tupperdate Auth.
 *
 * @param configuration the [Configuration] to use for tupperdat authentication.
 */
class TupperdateAuthenticationProvider(
    configuration: Configuration,
) : AuthenticationProvider(configuration) {

    internal val authenticationFunction = configuration.authenticationFunction

    /**
     * A [Configuration] for Firebase Authentication.
     */
    class Configuration internal constructor(
        auth: AccountFacade,
        name: String?
    ) : AuthenticationProvider.Configuration(name) {
        internal var authenticationFunction: AuthenticationFunction<String> = { token ->
            try {
                // Check if id token is valid and was issued before refresh tokens were revoked
                when (val result = auth.authenticate(Token(token))) {
                    is Result.Ok -> result.result.let(::TupperdateAuthPrincipal)
                    else -> null
                }
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
 * @param auth the [AccountFacade] to use for authentication.
 * @param configure a [Configuration] scope.
 */
fun Authentication.Configuration.tupperdate(
    auth: AccountFacade,
    configure: Configuration.() -> Unit = {},
) {
    val provider = TupperdateAuthenticationProvider(Configuration(auth, null).apply(configure))
    val authenticate = provider.authenticationFunction

    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->
        val token = call.request.bearerToken()
        val userId = token?.let { authenticate(call, it) }

        val failure = when {
            token == null -> AuthenticationFailedCause.NoCredentials
            userId == null -> AuthenticationFailedCause.InvalidCredentials
            else -> null
        }

        if (failure != null) {
            context.challenge(TupperdateAuthChallengeKey, failure) {
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
private fun ApplicationRequest.bearerToken(): String? {
    val data = header("Authorization") ?: return null
    if (!data.startsWith("Bearer")) return null
    return data.replaceFirst("Bearer", "").trim()
}

/**
 * Returns the [TupperdateAuthPrincipal] that is associated with the currently logged in user.
 *
 * TODO : Maybe we do not want to use this, if we offer additional authentication schemes.
 */
val ApplicationCall.tupperdateAuthPrincipal: TupperdateAuthPrincipal?
    get() = this.authentication.principal()

// Challenge identification specific to firestore.
private const val TupperdateAuthChallengeKey = "FirebaseAuth"
