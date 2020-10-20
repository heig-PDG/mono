package tupperdate.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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
     * A data class representing a user profile in the application.
     *
     * @param displayName might be null if the user has not set up their profile yet.
     * @param profileImageUrl might be null if the user does not have a profile pic.
     */
    data class Profile(
        val displayName: String?,
        val phoneNumber: String?,
        val profileImageUrl: String?,
    )

    /**
     * A [Flow] that returns the currently connected user [Profile].
     */
    val profile: Flow<Profile?>

    /**
     * A [Flow] that returns whether the user is currently connected or not.
     */
    val connected: Flow<Boolean>
        get() = profile.map { it != null }
}