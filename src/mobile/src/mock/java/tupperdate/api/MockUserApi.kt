package tupperdate.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

object MockUserApi : UserApi {
    private val mutableProfile: MutableStateFlow<UserApi.Profile?> = MutableStateFlow(null)

    override val profile: Flow<UserApi.Profile?>
        get() = mutableProfile

    override suspend fun updateProfile() {
    }

    override suspend fun putProfile(name: String) {
        mutableProfile.value = UserApi.Profile(
            name,
            "https://www.thispersondoesnotexist.com/image"
        )
    }

}
