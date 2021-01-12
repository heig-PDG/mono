package tupperdate.android.data.features.messages.store

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.FetcherResult
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.messages.ConversationIdentifier
import tupperdate.common.dto.MessageDTO

@InternalDataApi
class MessagesFetcher(
    private val client: HttpClient,
) : Fetcher<ConversationIdentifier, List<MessageDTO>> {

    override fun invoke(
        key: ConversationIdentifier,
    ) = flow {

        // Until we've reached a satisfying result...
        var keepFetching = true
        while (keepFetching) {
            try {
                // ... fetch the messages.
                val dto = client.get<List<MessageDTO>>("/chats/${key}/messages")
                emit(FetcherResult.Data(dto))
                keepFetching = false

                // TODO : Handle bad client requests gracefully.

            } catch (throwable: Throwable) {
                // ... or handle bad connectivity.
                throwable.printStackTrace()
                emit(FetcherResult.Error.Exception(throwable))

            }

            // Suspend or cancel if needed.
            if (keepFetching) delay(LinearBackoffMillis)
        }
    }
}


// TODO : Eventually use system connectivity.
private const val LinearBackoffMillis = 4 * 1000L // A good compromise between UX and perf.
