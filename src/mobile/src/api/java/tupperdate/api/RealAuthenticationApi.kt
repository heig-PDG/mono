package tupperdate.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object RealAuthenticationApi : AuthenticationApi {

    override suspend fun connect(email: String): AuthenticationApi.Error? {
        return AuthenticationApi.Error("Internal error.")
    }

    override suspend fun confirm(code: String): AuthenticationApi.Error? {
        return AuthenticationApi.Error("Internal error.")
    }

    override fun connectedUser(): Flow<AuthenticationApi.User?> {
        return flowOf(null)
    }
}