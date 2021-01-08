package tupperdate.android.data.features.auth.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.features.auth.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.auth.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import tupperdate.android.BuildConfig
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

    private val persistedToken = MutableStateFlow<String?>(null)

    override val sendWithoutRequest = true

    override suspend fun addRequestHeaders(request: HttpRequestBuilder) {
        val token = this.persistedToken.value ?: auth.currentUserFlow
            .filterNotNull()
            .first()
            .getIdToken(false)
            .await()
            .token
            .apply { if (BuildConfig.DEBUG) Log.d("Toky", this ?: "Token not retrieved yet.") }
        ?: return // TODO : Should failed requests be retried here or at a higher level ?

        persistedToken.value = token
        request.header(HttpHeaders.Authorization, "Bearer $token")
    }

    override fun isApplicable(auth: HttpAuthHeader): Boolean {
        return true
    }
}
