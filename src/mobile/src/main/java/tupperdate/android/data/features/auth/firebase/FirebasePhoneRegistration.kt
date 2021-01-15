package tupperdate.android.data.features.auth.firebase

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.coroutines.flow.MutableStateFlow
import tupperdate.android.data.RequiresParameterInjection
import tupperdate.android.data.features.auth.PhoneRegistration
import tupperdate.android.data.features.auth.PhoneRegistration.RequestCodeResult
import tupperdate.android.data.features.auth.PhoneRegistration.RequestCodeResult.*
import tupperdate.android.data.features.auth.PhoneRegistration.VerificationResult
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@RequiresParameterInjection
class FirebasePhoneRegistration(
    private val activity: Activity,
) : PhoneRegistration {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

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
        force: Boolean,
    ): RequestCodeResult {
        // 1. Check preconditions.
        if (number.isBlank()) return InvalidNumberError

        // 2. Use FirebaseAuth SDK.
        return suspendCoroutine { continuation ->

            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                private val isResumed = AtomicBoolean(false)

                private fun resumeOnce(f: () -> Unit) {
                    if (isResumed.compareAndSet(false, true)) {
                        f()
                    }
                }

                override fun onVerificationCompleted(
                    credential: PhoneAuthCredential,
                ) {
                    firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener { result ->
                            if (result.isSuccessful) {
                                resumeOnce {
                                    continuation.resume(LoggedIn)
                                }
                            } else {
                                resumeOnce {
                                    continuation.resume(InternalError)
                                    result.exception?.printStackTrace()
                                }
                            }
                        }
                }

                override fun onVerificationFailed(
                    problem: FirebaseException,
                ) {
                    when (problem) {
                        is FirebaseAuthInvalidCredentialsException -> resumeOnce {
                            continuation.resume(
                                InvalidNumberError
                            )
                        }
                        else -> resumeOnce {
                            problem.printStackTrace()
                            continuation.resume(
                                InternalError
                            )
                        }
                    }
                }

                override fun onCodeSent(
                    verification: String,
                    token: PhoneAuthProvider.ForceResendingToken,
                ) {
                    this@FirebasePhoneRegistration.verification.value = verification
                    this@FirebasePhoneRegistration.forceToken.value = token
                    resumeOnce {
                        continuation.resume(RequiresVerification)
                    }
                }
            }

            val token = forceToken.value
            val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .apply {
                    if (token != null && force) {
                        setForceResendingToken(token)
                    }
                }
                .setActivity(activity)
                .setPhoneNumber(number)
                .setTimeout(25, TimeUnit.SECONDS)
                .setCallbacks(callbacks)
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    override suspend fun verify(code: String): VerificationResult {
        if (code.isBlank()) return VerificationResult.InvalidVerificationError
        val verification = this@FirebasePhoneRegistration.verification.value
        return if (verification == null) {
            VerificationResult.InternalError
        } else {
            suspendCoroutine { continuation ->
                firebaseAuth.signInWithCredential(
                    PhoneAuthProvider.getCredential(
                        verification,
                        code
                    )
                ).addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        continuation.resume(VerificationResult.LoggedIn)
                    } else {
                        continuation.resume(VerificationResult.InvalidVerificationError)
                    }
                }
            }
        }
    }
}
