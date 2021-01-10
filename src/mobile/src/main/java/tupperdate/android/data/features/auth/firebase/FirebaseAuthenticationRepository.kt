package tupperdate.android.data.features.auth.firebase

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.dropAfterInstance
import tupperdate.android.data.features.auth.AuthenticationRepository
import tupperdate.android.data.features.auth.AuthenticationRepository.ProfileResult
import tupperdate.android.data.features.auth.AuthenticationStatus
import tupperdate.android.data.features.picker.ImagePicker
import tupperdate.android.data.readFileAndCompressAsBase64
import tupperdate.common.dto.MyUserDTO
import tupperdate.common.dto.UserDTO

@OptIn(InternalDataApi::class)
class FirebaseAuthenticationRepository(
    private val context: Context,
    private val client: HttpClient,
    auth: FirebaseAuth = FirebaseAuth.getInstance(),
) : AuthenticationRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val status: Flow<AuthenticationStatus> = auth.currentUserFlow.transformLatest { user ->

        // Handle non-authenticated users.
        if (user == null) {
            emit(AuthenticationStatus.None)
            return@transformLatest
        }

        // Start loading the profile information.
        emit(AuthenticationStatus.LoadingProfile(user.uid))

        // TODO : Eventually handle backoff, retries, etc. Maybe use WorkManager ?
        try {
            val userDTO: UserDTO = client.get("/users/${user.uid}")
            emit(
                AuthenticationStatus.CompleteProfile(
                    identifier = user.uid,
                    phoneNumber = user.phoneNumber!!,
                    displayName = userDTO.displayName!!,
                    displayPictureUrl = userDTO.picture,
                )
            )
        } catch (t: Throwable) {
            emit(
                AuthenticationStatus.AbsentProfile(
                    identifier = user.uid,
                )
            )
        }
    }
        // Only allow upgrades.
        .dropAfterInstance<AuthenticationStatus, AuthenticationStatus.Identified>()
        .dropAfterInstance<AuthenticationStatus, AuthenticationStatus.Loaded>()
        .dropAfterInstance<AuthenticationStatus, AuthenticationStatus.Displayable>()

    override suspend fun updateProfile(
        displayName: String,
        picture: ImagePicker.Handle?
    ): ProfileResult {

        val connected = status.filterIsInstance<AuthenticationStatus.Identified>().first()

        return try {
            // TODO : Reload the internal profile.
            // TODO : Use the WorkManager instead.
            client.put<UserDTO>("/users/${connected.identifier}") {
                body = MyUserDTO(
                    displayName = displayName,
                    imageBase64 = picture?.uri?.readFileAndCompressAsBase64(context.contentResolver),
                )
            }
            ProfileResult.Success
        } catch (t: Throwable) {
            ProfileResult.BadLogin
        }
    }
}
