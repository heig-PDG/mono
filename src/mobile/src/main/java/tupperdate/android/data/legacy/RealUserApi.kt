package tupperdate.android.data.legacy

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import tupperdate.android.data.legacy.api.UserApi
import tupperdate.android.data.legacy.api.readFileAsBase64
import tupperdate.common.dto.MyUserDTO
import tupperdate.common.dto.UserDTO

/**
 * Extension method that will transform a [UserDTO] to a [UserApi.Profile]
 */
private fun UserDTO.toProfile(): UserApi.Profile {
    return UserApi.Profile(
        displayName = this.displayName,
        profileImageUrl = this.picture,
    )
}

class RealUserApi(
    private val client: HttpClient,
    private val uid: Flow<String?>,
    private val contentResolver: ContentResolver,
) : UserApi {
    private val mutableProfile: MutableStateFlow<UserApi.Profile?> = MutableStateFlow(null)

    override val profile: Flow<UserApi.Profile?> = mutableProfile

    override suspend fun updateProfile() {
        val currentUid = uid.filterNotNull().first()

        try {
            val userDTO : UserDTO = client.get("/users/$currentUid")
            mutableProfile.value = userDTO.toProfile()
        } catch (t: Throwable) {
            Log.d("UserDebug", t.message.toString())
        }
    }

    override suspend fun putProfile(name: String, imageUri: Uri?) {
        val currentUid = uid.filterNotNull().first()
        val oldProfile = mutableProfile.value

        try {
            // Update locally
            mutableProfile.value = UserApi.Profile(
                name,
                oldProfile?.profileImageUrl,
            )

            // Send to server
            client.put<UserDTO>("/users/$currentUid") {
                body = MyUserDTO(
                    displayName = name,
                    imageBase64 = imageUri?.readFileAsBase64(contentResolver),
                )
            }
        } catch (t: Throwable) {
            Log.d("UserDebug", t.message.toString())

            // If error, reset old value
            mutableProfile.value = oldProfile
        }
    }
}
