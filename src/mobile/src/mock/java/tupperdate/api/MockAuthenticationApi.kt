package tupperdate.api

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
object MockAuthenticationApi : AuthenticationApi {

    private val user = MutableStateFlow<AuthenticationApi.Profile?>(null)

    override suspend fun requestCode(
        number: String,
        force: Boolean,
    ): AuthenticationApi.RequestCodeResult {
        val timer = Random.nextLong(from = 1000, until = 3000)
        delay(timer)
        return when (number) {
            "144" -> {
                user.value = AuthenticationApi.Profile(
                    displayName = null,
                    phoneNumber = "144",
                    profileImageUrl = null,
                )
                AuthenticationApi.RequestCodeResult.LoggedIn
            }
            "123" -> AuthenticationApi.RequestCodeResult.RequiresVerification
            "666" -> AuthenticationApi.RequestCodeResult.InternalError
            else -> AuthenticationApi.RequestCodeResult.InvalidNumberError
        }
    }

    override suspend fun verify(code: String): AuthenticationApi.VerificationResult {
        val timer = Random.nextLong(from = 1000, until = 3000)
        delay(timer)
        return when (code) {
            "0000" -> {
                user.value = AuthenticationApi.Profile(
                    displayName = null,
                    phoneNumber = "144",
                    profileImageUrl = null,
                )
                AuthenticationApi.VerificationResult.LoggedIn
            }
            "666" -> AuthenticationApi.VerificationResult.InternalError
            else -> AuthenticationApi.VerificationResult.InvalidVerificationError
        }
    }

    override val profile: Flow<AuthenticationApi.Profile?>
        get() = user

    override val auth: Flow<AuthenticationApi.AuthInfo?>
        get() = user.map { AuthenticationApi.AuthInfo("toky the token") }
}
