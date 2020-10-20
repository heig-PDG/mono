package tupperdate.api

import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalCoroutinesApi::class)
class RealAuthenticationApi(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : AuthenticationApi {

    /**
     * The [PhoneAuthProvider.ForceResendingToken] that might be used if the force parameter is
     * requested when sending a new code.
     */
    private val forceToken = MutableStateFlow<PhoneAuthProvider.ForceResendingToken?>(null)

    /**
     * A [String] that will be used to perform phone number verification.
     */
    private val verification = MutableStateFlow<String?>(null)

    override suspend fun requestCode(
        number: String,
        force: Boolean
    ): AuthenticationApi.RequestCodeResult = suspendCoroutine { continuation ->

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(
                credential: PhoneAuthCredential,
            ) {
                continuation.resume(AuthenticationApi.RequestCodeResult.LoggedIn)
            }

            override fun onVerificationFailed(
                problem: FirebaseException,
            ) {
                when (problem) {
                    is FirebaseAuthInvalidCredentialsException -> continuation.resume(
                        AuthenticationApi.RequestCodeResult.InvalidNumberError
                    )
                    else -> continuation.resume(
                        AuthenticationApi.RequestCodeResult.InternalError
                    )
                }
            }

            override fun onCodeSent(
                verification: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                this@RealAuthenticationApi.verification.value = verification
                this@RealAuthenticationApi.forceToken.value = token
                continuation.resume(AuthenticationApi.RequestCodeResult.RequiresVerification)
            }

            override fun onCodeAutoRetrievalTimeOut(
                verification: String,
            ) {
                this@RealAuthenticationApi.verification.value = verification
                continuation.resume(AuthenticationApi.RequestCodeResult.RequiresVerification)
            }
        }

        val token = forceToken.value
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .apply {
                if (token != null && force) {
                    setForceResendingToken(token)
                }
            }
            .setPhoneNumber(number)
            .setTimeout(25, TimeUnit.SECONDS)
            .setCallbacks(callbacks)
            .setExecutor(TaskExecutors.MAIN_THREAD)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override suspend fun verify(code: String): AuthenticationApi.VerificationResult {
        TODO("Not yet implemented")
    }

    override val profile: Flow<AuthenticationApi.Profile?>
        get() = currentUser(firebaseAuth)
            // TODO : Retrieve some additional profile data.
            .map {
                it?.let { user ->
                    AuthenticationApi.Profile(
                        displayName = null,
                        phoneNumber = user.phoneNumber,
                        profileImageUrl = null,
                    )
                }
            }
}

/**
 * Returns a [Flow] of the current [FirebaseUser], and emits one on every authentication change.
 *
 * @param auth the [FirebaseAuth] that's used to retrieve the user.
 */
@OptIn(ExperimentalCoroutinesApi::class)
private fun currentUser(auth: FirebaseAuth): Flow<FirebaseUser?> {
    return callbackFlow {
        val callback = FirebaseAuth.AuthStateListener { auth ->
            safeOffer(auth.currentUser)
        }
        auth.addAuthStateListener(callback)
        awaitClose { auth.removeAuthStateListener((callback)) }
    }
}

/**
 * Safely offers an item in a [SendChannel]. If the [SendChannel] is closed, no exception will be
 * thrown. This might be useful in scenarios where recomposition happens while an operation is
 * being performed.
 */
fun <T> SendChannel<T>.safeOffer(element: T): Boolean {
    return kotlin.runCatching { offer(element) }.getOrDefault(false)
}