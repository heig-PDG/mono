package tupperdate.api

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

object MockUserApi : UserApi {
    private val mutableProfile: MutableStateFlow<UserApi.Profile?> = MutableStateFlow(null)

    override val profile: Flow<UserApi.Profile?>
        get() = mutableProfile

    override suspend fun updateProfile() {
    }

    override suspend fun putProfile(name: String, imageUri: Uri?) {
        mutableProfile.value = UserApi.Profile(
            name,
            imageUri.toString(),
        )
    }

}
