package tupperdate.api

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tupperdate.common.dto.MyUserDTO
import tupperdate.common.dto.UserDTO

class RealUserApi(
    private val client: HttpClient,
    private val uid: Flow<String?>,
) : UserApi {

    override val profile: Flow<UserApi.Profile?>
        get() = uid.map { uid ->
            try {
                val currentProfile = client.get<UserDTO>("/users/$uid")

                return@map UserApi.Profile(
                    displayName = currentProfile.displayName,
                    profileImageUrl = currentProfile.picture,
                )
            } catch (t: Throwable) {
                return@map null
            }
        }

    override suspend fun updateProfile(name: String) {
        uid.map {
            client.put<Unit>("/users/$uid") {
                body = MyUserDTO(name)
            }
        }
    }

}
