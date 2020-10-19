package tupperdate.api

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
object MockAuthenticationApi : AuthenticationApi {

    private val user = MutableStateFlow<AuthenticationApi.User?>(null)

    override suspend fun connect(email: String): AuthenticationApi.Error? {
        val timer = Random.nextLong(from = 1000, until = 3000)
        delay(timer)
        return if (email.contains("fail")) {
            AuthenticationApi.Error("Something went wrong.")
        } else {
            null
        }
    }

    override suspend fun confirm(code: String): AuthenticationApi.Error? {
        val timer = Random.nextLong(from = 1000, until = 3000)
        delay(timer)
        if (code == "0000") {
            user.value = AuthenticationApi.User(
                email = "olivier@heig-vd.ch",
                displayName = "Olivier Liechti",
                profileImageUrl = null,
            )
            return null
        } else {
            return AuthenticationApi.Error("Bad code.")
        }
    }

    override fun connectedUser(): Flow<AuthenticationApi.User?> {
        return user
    }
}