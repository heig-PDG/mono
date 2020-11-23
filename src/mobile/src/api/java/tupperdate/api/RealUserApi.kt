package tupperdate.api

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tupperdate.common.dto.UserDTO

class RealUserApi(
    private val client: HttpClient,
    private val uid: Flow<String?>,
) : UserApi {

    override val profile: Flow<UserApi.Profile?>
        get() = uid.map { uid ->
            val currentProfile = client.get<UserDTO>("/users/$uid")

            return@map UserApi.Profile(
                displayName = currentProfile.displayName,
                profileImageUrl = currentProfile.picture,
            )
        }

    override fun updateProfile(profile: UserApi.Profile) {
        TODO("Not yet implemented")
    }

}
