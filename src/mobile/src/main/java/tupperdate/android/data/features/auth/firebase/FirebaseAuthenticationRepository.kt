package tupperdate.android.data.features.auth.firebase

import androidx.work.WorkManager
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.SyncRequestBuilder
import tupperdate.android.data.dropAfterInstance
import tupperdate.android.data.features.auth.AuthenticationRepository
import tupperdate.android.data.features.auth.AuthenticationRepository.ProfileResult
import tupperdate.android.data.features.auth.AuthenticationStatus
import tupperdate.android.data.features.auth.store.ProfileFetcher
import tupperdate.android.data.features.auth.store.ProfileSourceOfTruth
import tupperdate.android.data.features.auth.work.RefreshProfileWorker
import tupperdate.android.data.features.auth.work.UpdateProfileWorker
import tupperdate.android.data.features.picker.ImagePicker
import tupperdate.android.data.room.TupperdateDatabase

@OptIn(InternalDataApi::class)
class FirebaseAuthenticationRepository(
    private val client: HttpClient,
    private val work: WorkManager,
    private val database: TupperdateDatabase,
    auth: FirebaseAuth = FirebaseAuth.getInstance(),
) : AuthenticationRepository {

    override val identifier: Flow<AuthenticationStatus.Identified> = auth.currentUserFlow
        .mapNotNull { it?.uid }
        .map { AuthenticationStatus.LoadingProfile(it) }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    override val status: Flow<AuthenticationStatus> = auth.currentUserFlow.transformLatest { user ->

        // Handle non-authenticated users.
        if (user == null) {
            emit(AuthenticationStatus.None)
            return@transformLatest
        }

        // Create a store with cached profile requests.
        val request = StoreRequest.cached(user.uid, refresh = true)
        val store = StoreBuilder.from(
            fetcher = ProfileFetcher(client),
            sourceOfTruth = ProfileSourceOfTruth(database.profiles())
        ).build()

        // Prepare a streaming flow.
        val stream = store.stream(request)
            .map {
                it.throwIfError()
                it.dataOrNull()
            }
            .filterNotNull()
            .catch { it.printStackTrace() }

        // Fetch and emit values.
        emitAll(stream)
    }
        // Only allow upgrades.
        .dropAfterInstance<AuthenticationStatus, AuthenticationStatus.Identified>()
        .dropAfterInstance<AuthenticationStatus, AuthenticationStatus.Loaded>()
        .dropAfterInstance<AuthenticationStatus, AuthenticationStatus.Displayable>()

    override suspend fun updateProfile(
        displayName: String,
    ) = requestUpdateProfile(
        displayName = displayName,
        picture = null,
        updateName = true,
        updatePicture = false,
    )

    override suspend fun updateProfile(
        picture: ImagePicker.Handle?,
    ) = requestUpdateProfile(
        displayName = "",
        picture = picture,
        updateName = false,
        updatePicture = true,
    )

    override suspend fun updateProfile(
        displayName: String,
        picture: ImagePicker.Handle?
    ) = requestUpdateProfile(
        displayName = displayName,
        picture = picture,
        updateName = true,
        updatePicture = true,
    )

    private suspend fun requestUpdateProfile(
        displayName: String,
        picture: ImagePicker.Handle?,
        updateName: Boolean,
        updatePicture: Boolean,
    ): ProfileResult {
        val connected = status.filterIsInstance<AuthenticationStatus.Identified>().first()

        val update = SyncRequestBuilder<UpdateProfileWorker>()
            .setInputData(
                UpdateProfileWorker.Data(
                    uid = connected.identifier,
                    name = displayName,
                    picture = picture?.uri?.toString(),
                    updateName = updateName,
                    updatePicture = updatePicture,
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
