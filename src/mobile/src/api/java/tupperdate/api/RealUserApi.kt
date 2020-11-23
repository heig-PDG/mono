package tupperdate.api

import android.util.Log
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import tupperdate.common.dto.MyUserDTO
import tupperdate.common.dto.UserDTO

class RealUserApi(
    private val client: HttpClient,
    private val uid: Flow<String?>,
) : UserApi {

    override fun profile(): Flow<UserApi.Profile?> = flow {
        val currentUid = uid.filterNotNull().first()
        var profile:UserApi.Profile? = null

        try {
            val currentProfile = client.get<UserDTO>("/users/$currentUid")

            Log.d("UserDebug", currentProfile.displayName)

            profile = UserApi.Profile(
                displayName = currentProfile.displayName,
                profileImageUrl = currentProfile.picture,
            )
        } catch (t: Throwable) {
            Log.d("UserDebug", t.message.toString())
        }

        emit(profile)
    }

    override suspend fun updateProfile(name: String) {
        val currentUid = uid.filterNotNull().first()

        try {
            Log.d("UserDebug", "uid: $currentUid")
            val result = client.put<UserDTO>("/users/$currentUid") {
                body = MyUserDTO(name)
            }
            Log.d("UserDebug", "name: $result.displayName")
        } catch (t: Throwable) {
            Log.d("UserDebug", t.message.toString())
        }
    }

}
