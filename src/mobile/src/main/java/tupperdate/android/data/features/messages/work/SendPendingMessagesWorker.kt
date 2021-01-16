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
        // 1. Clean up the database of pending messages that were already sent.
        cleanupSentAndReceived()

        // 2. Sync new pending messages.
        val pending = database.messages().pendingToSend().first()
        var succcess = true
        for (message in pending) {
            try {
                client.post<Unit>("/chats/${message.recipient}/messages") {
                    body = MessageContentDTO(
                        content = message.body,
                        tempId = message.identifier,
                    )
                }
                database.messages().pendingMarkSent(message.identifier)
            } catch (throwable: Throwable) {
                // TODO : Handle client exceptions.
                succcess = false
            }
        }
        return if (succcess) Result.success() else Result.retry()
    }

    private suspend fun cleanupSentAndReceived() {
        val sent = database.messages().pendingSent().first()
        for (message in sent) {
            database.messages().pendingClear(message.identifier)
        }
    }
}
