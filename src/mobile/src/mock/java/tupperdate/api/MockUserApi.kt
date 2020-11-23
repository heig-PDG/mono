package tupperdate.api

import kotlinx.coroutines.flow.Flow

object MockUserApi : UserApi {
    override val profile: Flow<UserApi.Profile?>
        get() = TODO("Not yet implemented")

    override fun updateProfile(profile: UserApi.Profile) {
        TODO("Not yet implemented")
    }

}
