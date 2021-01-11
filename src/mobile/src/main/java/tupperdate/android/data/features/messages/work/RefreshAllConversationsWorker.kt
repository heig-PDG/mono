package tupperdate.android.data.features.messages.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.fresh
import io.ktor.client.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.messages.store.AllConversationFetcher
import tupperdate.android.data.features.messages.store.AllConversationSourceOfTruth
import tupperdate.android.data.room.TupperdateDatabase

@InternalDataApi
@OptIn(
    ExperimentalCoroutinesApi::class,
    FlowPreview::class,
    KoinApiExtension::class,
)
class RefreshAllConversationsWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val client by inject<HttpClient>()
    private val database by inject<TupperdateDatabase>()

    override suspend fun doWork(): Result {
        val store = StoreBuilder.from(
            AllConversationFetcher(client),
            AllConversationSourceOfTruth(database.messages()),
        ).build()
        store.fresh(Unit)
        return Result.success()
    }
}
