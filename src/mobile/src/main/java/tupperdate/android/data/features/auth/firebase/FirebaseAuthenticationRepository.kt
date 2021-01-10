package tupperdate.android.data.features.auth.firebase

import android.content.Context
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.SyncRequestBuilder
import tupperdate.android.data.dropAfterInstance
import tupperdate.android.data.features.auth.AuthenticationRepository
import tupperdate.android.data.features.auth.AuthenticationRepository.ProfileResult
import tupperdate.android.data.features.auth.AuthenticationStatus
import tupperdate.android.data.features.auth.work.RefreshProfileWorker
import tupperdate.android.data.features.auth.work.UpdateProfileWorker
import tupperdate.android.data.features.picker.ImagePicker
import tupperdate.common.dto.UserDTO

@OptIn(InternalDataApi::class)
class FirebaseAuthenticationRepository(
    private val context: Context,
    private val client: HttpClient,
    private val work: WorkManager,
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

        val update = SyncRequestBuilder<UpdateProfileWorker>()
            .setInputData(
                UpdateProfileWorker.Data(
                    uid = connected.identifier,
                    name = displayName,
                    picture = picture?.uri?.toString(),
                )
            )
            .build()
        val refresh = SyncRequestBuilder<RefreshProfileWorker>()
            .build()

        // Update, then refresh data.
        work.beginWith(update)
            .then(refresh)
            .enqueue()

        // Optimistic handling.
        return ProfileResult.Success
    }
}
