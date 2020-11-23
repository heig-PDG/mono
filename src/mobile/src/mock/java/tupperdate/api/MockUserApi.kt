package tupperdate.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object MockUserApi : UserApi {
    override fun profile(): Flow<UserApi.Profile?> = flowOf(null)

    override suspend fun updateProfile(name: String) {
    }

}
