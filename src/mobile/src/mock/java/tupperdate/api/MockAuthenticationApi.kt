package tupperdate.api

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
object MockAuthenticationApi : AuthenticationApi {
    private val localConnected = MutableStateFlow(false)

    override suspend fun requestCode(
        number: String,
        force: Boolean,
    ): AuthenticationApi.RequestCodeResult {
        val timer = Random.nextLong(from = 1000, until = 3000)
        delay(timer)
        return when (number) {
            "144" -> {
                localConnected.value = true
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
                localConnected.value = true
                AuthenticationApi.VerificationResult.LoggedIn
            }
            "666" -> AuthenticationApi.VerificationResult.InternalError
            else -> AuthenticationApi.VerificationResult.InvalidVerificationError
        }
    }

    override val connected: Flow<Boolean>
        get() = localConnected

    override val uid: Flow<String?>
        get() = flowOf(null)
}
