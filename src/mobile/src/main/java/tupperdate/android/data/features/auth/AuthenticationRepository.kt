package tupperdate.android.data.features.auth

import kotlinx.coroutines.flow.Flow
import tupperdate.android.data.features.picker.ImagePicker

interface AuthenticationRepository {

    val status: Flow<AuthenticationStatus>

    suspend fun updateProfile(
        displayName: String,
        picture: ImagePicker.Handle? = null,
    ): ProfileResult

    enum class ProfileResult {
        Success,
        BadLogin,
    }
}
