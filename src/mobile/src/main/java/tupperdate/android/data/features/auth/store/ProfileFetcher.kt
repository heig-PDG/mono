package tupperdate.android.data.features.auth.store

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.FetcherResult
import com.dropbox.android.external.store4.FetcherResult.Data
import com.dropbox.android.external.store4.FetcherResult.Error.Exception
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.AuthenticationStatus
import tupperdate.android.data.features.auth.AuthenticationStatus.*
import tupperdate.android.data.features.auth.firebase.FirebaseUid
import tupperdate.common.dto.UserDTO

/**
 * A [Fetcher] that emits [AuthenticationStatus]. If necessary, it will take care of retrying
 * requests until a [UserDTO] can be fetched, or the server tells us that no such DTO exists and
 * that the user profile has not been set yet.
 *
 * @param client the [HttpClient] that is used to fetch the profiles from the server.
 */
@InternalDataApi
@OptIn(FlowPreview::class)
class ProfileFetcher(
    private val client: HttpClient,
) : Fetcher<FirebaseUid, AuthenticationStatus> {

    override fun invoke(
        key: FirebaseUid,
    ) = flow<FetcherResult<AuthenticationStatus>> {
        emit(Data(LoadingProfile(key)))

        // Until we've reached a satisfying result...
        var keepFetching = true
        while (keepFetching) {
            try {
                // ... fetch the user profile.
                val dto = client.get<UserDTO>("/users/${key}")
                val profile = CompleteProfile(
                    identifier = key,
                    phoneNumber = "012 345 67 89", // TODO : Fetch this from the API (issue #200).
                    displayName = dto.displayName ?: "",
                    displayPictureUrl = dto.picture,
                )
                emit(Data(profile))
                keepFetching = false

            } catch (clientException: ClientRequestException) {
                // ... or learn that the profile is not set yet.
                if (HttpStatusCode.NotFound == clientException.response.status) {
                    emit(Data(AbsentProfile(key)))
                    keepFetching = false
                }

            } catch (throwable: Throwable) {
                // ... or handle bad connectivity.
                throwable.printStackTrace()
                emit(Exception(throwable))

            }

            // Suspend or cancel if needed.
            if (keepFetching) delay(LinearBackoffMillis)
        }
    }
}

private const val LinearBackoffMillis = 4 * 1000L // A good compromise between UX and perf.
