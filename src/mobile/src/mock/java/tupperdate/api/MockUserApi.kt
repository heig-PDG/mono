package tupperdate.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object MockUserApi : UserApi {
    override val profile: Flow<UserApi.Profile?>
        get() = flowOf(null)

    override suspend fun updateProfile(name: String) {
    }

}
