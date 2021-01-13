package tupperdate.android.data.features.messages.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.fresh
import io.ktor.client.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.AuthenticationRepository
import tupperdate.android.data.features.messages.ConversationIdentifier
import tupperdate.android.data.features.messages.store.MessagesFetcher
import tupperdate.android.data.features.messages.store.MessagesSourceOfTruth
import tupperdate.android.data.room.TupperdateDatabase


@InternalDataApi
@OptIn(
    ExperimentalCoroutinesApi::class,
    FlowPreview::class,
    KoinApiExtension::class,
)
class RefreshMessagesWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val auth by inject<AuthenticationRepository>()
    private val client by inject<HttpClient>()
    private val database by inject<TupperdateDatabase>()


    override suspend fun doWork(): Result {
        val id = requireNotNull(inputData.getString(KeyId))
        val store = StoreBuilder.from(
            fetcher = MessagesFetcher(client),
            sourceOfTruth = MessagesSourceOfTruth(auth, database.messages()),
        ).build()
        store.fresh(id)
        return Result.success()
    }

    companion object {

        private const val KeyId = "conversationId"

        fun forConversation(id: ConversationIdentifier): Data {
            return workDataOf(KeyId to id)
        }
    }
}
