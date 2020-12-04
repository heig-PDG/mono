package tupperdate.android.data.legacy.api

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface UserApi {
    /**
     * A data class representing a user profile in the application.
     *
     * @param displayName might be null if the user has not set up their profile yet.
     * @param profileImageUrl might be null if the user does not have a profile pic.
     */
    data class Profile(
        val displayName: String?,
        val profileImageUrl: String?,
    )

    val emptyProfile: Profile
        get() = Profile("", "",)

    /**
     * A [Flow] that returns the currently connected user [Profile].
     */
    val profile: Flow<Profile?>

    /**
     * A function that will update the local [Profile] value
     */
    suspend fun updateProfile()

    /**
     * A method that will allow us to update our [Profile]
     */
    suspend fun putProfile(name: String, imageUri: Uri?)
}
