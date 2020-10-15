package tupperdate.api

import kotlinx.coroutines.flow.Flow

interface AuthenticationApi {

    data class User(
        val email: String,
        val displayName: String,
        val profileImageUrl: String?,
    )

    data class Error(val message: String)

    suspend fun connect(email: String): Error?
    suspend fun confirm(code: String): Error?

    fun connectedUser(): Flow<User?>
}