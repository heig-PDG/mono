package tupperdate.android.data.features.auth

import kotlinx.coroutines.flow.Flow
import tupperdate.android.data.features.picker.ImagePicker

interface AuthenticationRepository {

    val status: Flow<AuthenticationStatus>
    val identifier: Flow<AuthenticationStatus.Identified>

    /**
     * Partially updates the profile, preserving the existing profile picture.
     *
     * @param displayName the name to use.
     */
    suspend fun updateProfile(
        displayName: String,
    ): ProfileResult

    /**
     * Partially updates the profile, preserving the existing display name.
     *
     * @param picture the handle to the picture to use.
     */
    suspend fun updateProfile(
        picture: ImagePicker.Handle?,
    ): ProfileResult

    /**
     * Updates the whole profile, setting both the display name and the profile picture.
     *
     * @param displayName the name to use.
     * @param picture the handle to the picture to use.
     */
    suspend fun updateProfile(
        displayName: String,
        picture: ImagePicker.Handle?,
    ): ProfileResult

    enum class ProfileResult {
        Success,
        BadLogin,
    }
}
