package tupperdate.android.data.features.messages.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.room.TupperdateDatabase
import tupperdate.common.dto.MessageContentDTO

@InternalDataApi
@OptIn(KoinApiExtension::class)
class SendPendingMessagesWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val client by inject<HttpClient>()
    private val database by inject<TupperdateDatabase>()

    override suspend fun doWork(): Result {
        val pending = database.messages().pending().first()
        var succcess = true
        for (message in pending) {
            try {
                client.put<Unit>("/chats/${message.recipient}/messages") {
                    body = MessageContentDTO(content = message.body)
                }
                database.messages().pendingDelete(message.identifier)
            } catch (throwable: Throwable) {
                // TODO : Handle client exceptions.
                succcess = false
            }
        }
        return if (succcess) Result.success() else Result.retry()
    }
}
