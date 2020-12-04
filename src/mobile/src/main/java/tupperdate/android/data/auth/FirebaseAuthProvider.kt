package tupperdate.android.data.auth

import io.ktor.client.features.auth.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.auth.*
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import tupperdate.android.data.RealAuthenticationApi

/**
 * Registers the [RealAuthenticationApi] as a provider for all the authenticated requests. No HTTP
 * request will be sent before a user is properly authenticated via the [api].
 */
fun Auth.firebase(api: RealAuthenticationApi) {
    providers += FirebaseAuthProvider(api)
}

private class FirebaseAuthProvider(
    private val api: RealAuthenticationApi
) : AuthProvider {

    override val sendWithoutRequest = true

    override suspend fun addRequestHeaders(request: HttpRequestBuilder) {
        val token = api.auth.filterNotNull().first()
        request.header(HttpHeaders.Authorization, "Bearer ${token.token}")
    }

    override fun isApplicable(auth: HttpAuthHeader): Boolean {
        return true
    }
}
