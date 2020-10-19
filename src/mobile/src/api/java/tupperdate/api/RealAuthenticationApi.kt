package tupperdate.api

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.concurrent.TimeUnit

class RealAuthenticationApi(
    private val activity: AppCompatActivity,
) : AuthenticationApi {

    override suspend fun connect(email: String): AuthenticationApi.Error? {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            email,
            60,
            TimeUnit.SECONDS,
            activity,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                }

                override fun onVerificationFailed(exception: FirebaseException) {
                    exception.printStackTrace()
                }

                override fun onCodeAutoRetrievalTimeOut(p0: String) {
                    super.onCodeAutoRetrievalTimeOut(p0)
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(verificationId, token)
                }
            })
        return null
    }

    override suspend fun confirm(code: String): AuthenticationApi.Error? {
        return AuthenticationApi.Error("Internal error.")
    }

    override fun connectedUser(): Flow<AuthenticationApi.User?> {
        return flowOf(null)
    }
}