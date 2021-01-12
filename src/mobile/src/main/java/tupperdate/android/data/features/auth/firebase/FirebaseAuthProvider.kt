package tupperdate.android.data.features.auth.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.features.auth.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.auth.*
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.AuthenticationRepository

/**
 * Registers the [AuthenticationRepository] as a provider for all the authenticated requests. No HTTP
 * request will be sent before a user is properly authenticated via the API.
 */
@InternalDataApi
fun Auth.firebase(auth: FirebaseAuth = FirebaseAuth.getInstance()) {
    providers += AuthProvider(auth)
}

@InternalDataApi
private class AuthProvider(
    private val auth: FirebaseAuth
) : io.ktor.client.features.auth.AuthProvider {

    override val sendWithoutRequest = true

    override suspend fun addRequestHeaders(request: HttpRequestBuilder) {
        val token = auth.currentUserFlow.filterNotNull().first()
            .getIdToken(false).await().token

        // TODO : Remove or strip this in production.
        Log.d(
            "AuthProvider",
            "Authenticating ${request.method} request to ${request.url.buildString()} with $token",
        )

        request.header(HttpHeaders.Authorization, "Bearer $token")
    }

    override fun isApplicable(auth: HttpAuthHeader): Boolean {
        return true
    }
}
