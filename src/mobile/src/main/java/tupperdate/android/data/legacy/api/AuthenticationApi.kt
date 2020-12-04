package tupperdate.android.data.legacy.api

import kotlinx.coroutines.flow.Flow

interface AuthenticationApi {

    enum class RequestCodeResult {
        LoggedIn,
        RequiresVerification,
        InvalidNumberError,
        InternalError,
    }

    enum class VerificationResult {
        LoggedIn,
        InvalidVerificationError,
        InternalError,
    }

    data class AuthInfo(
        val token: String,
    )

    /**
     * Returns a [RequestCodeResult] indicating the status of the phone number verification. Some
     * verifications might succeed directly, on phones with the right version of Google Play
     * Services, while other devices might require some additional verification.
     *
     * @param number the phone number to verify. It must be provided with an international prefix.
     * @param force whether to force sending a new verification code. Unless explicitly asked by
     *              the user, you SHOULD NOT set this parameter to true.
     *
     * @return a [RequestCodeResult] that indicates how the code request went.
     */
    suspend fun requestCode(number: String, force: Boolean = false): RequestCodeResult

    /**
     * Verifies the identity of this device by providing a code. This should be called if the
     * [RequestCodeResult.RequiresVerification] result code was previously sent.
     *
     * @param code the verification code that's required to log in.
     *
     * @return a [VerificationResult] that indicates whether the user is successfully logged in or
     *         not.
     */
    suspend fun verify(code: String): VerificationResult

    /**
     * A [Flow] that returns whether the user is currently connected or not.
     */
    val connected: Flow<Boolean>

    val uid: Flow<String?>

    val auth: Flow<AuthInfo?>
}
