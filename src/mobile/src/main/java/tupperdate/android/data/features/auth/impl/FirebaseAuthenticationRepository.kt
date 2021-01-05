package tupperdate.android.data.features.auth.impl

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import org.apache.commons.codec.binary.Base64
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.AuthenticationRepository
import tupperdate.android.data.features.auth.AuthenticationRepository.ProfileResult
import tupperdate.android.data.features.auth.AuthenticationStatus
import tupperdate.android.data.features.picker.ImagePicker
import tupperdate.common.dto.MyUserDTO
import tupperdate.common.dto.UserDTO

@InternalDataApi
class FirebaseAuthenticationRepository(
    private val context: Context,
    private val client: HttpClient,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
) : AuthenticationRepository {

    override val status: Flow<AuthenticationStatus>
        get() = auth.currentUserFlow.map {
            // TODO : Drastically improve this.
            it?.let { user ->
                try {
                    val userDTO: UserDTO = client.get("/users/${user.uid}")
                    return@let AuthenticationStatus.Profile(
                        identifier = user.uid,
                        phoneNumber = user.phoneNumber!!,
                        token = user.getIdToken(false).await().token!!,
                        displayName = userDTO.displayName!!,
                        displayPictureUrl = userDTO.picture,
                    )
                } catch (t: Throwable) {
                    return@let AuthenticationStatus.NoProfile(
                        identifier = user.uid,
                        phoneNumber = user.phoneNumber!!,
                        token = user.getIdToken(false).await().token!!, // TODO : Handle bad conn.
                    )
                }
            } ?: AuthenticationStatus.NoAuthentication
        }

    override suspend fun updateProfile(
        displayName: String,
        picture: ImagePicker.Handle?
    ): ProfileResult {

        val connected = status.filterIsInstance<AuthenticationStatus.Connected>().first()

        return try {
            // TODO : Reload the internal profile.
            // TODO : Use the WorkManager instead.
            client.put<UserDTO>("/users/${connected.identifier}") {
                body = MyUserDTO(
                    displayName = displayName,
                    imageBase64 = picture?.uri?.readFileAsBase64(context.contentResolver),
                )
            }
            ProfileResult.Success
        } catch (t: Throwable) {
            ProfileResult.BadLogin
        }
    }
}

@InternalDataApi
private fun Uri.readFileAsBase64(contentResolver: ContentResolver): String? =
    contentResolver.openInputStream(this)
        ?.buffered()
        ?.readBytes()
        ?.let(Base64::encodeBase64String)
