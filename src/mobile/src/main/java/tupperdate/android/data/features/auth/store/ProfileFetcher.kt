package tupperdate.android.data.features.auth.store

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.FetcherResult
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.AuthenticationStatus
import tupperdate.common.dto.UserDTO

@InternalDataApi
@OptIn(FlowPreview::class)
class ProfileFetcher(
    private val client: HttpClient,
) : Fetcher<Pair<String, String>, AuthenticationStatus.CompleteProfile> {

    override fun invoke(
        key: Pair<String, String>,
    ): Flow<FetcherResult<AuthenticationStatus.CompleteProfile>> {
        return suspend {
            try {
                val (uid, phone) = key
                val dto = client.get<UserDTO>("/users/${uid}")
                val profile = AuthenticationStatus.CompleteProfile(
                    identifier = uid,
                    phoneNumber = phone,
                    displayName = dto.displayName ?: "",
                    displayPictureUrl = dto.picture,
                )
                FetcherResult.Data(profile)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
                FetcherResult.Error.Exception(throwable)
            }
        }.asFlow()
    }
}
