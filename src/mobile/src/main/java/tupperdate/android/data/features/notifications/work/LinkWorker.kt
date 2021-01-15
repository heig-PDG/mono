package tupperdate.android.data.features.notifications.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tupperdate.android.data.InternalDataApi
import tupperdate.common.dto.NewNotificationTokenDTO

/**
 * A [CoroutineWorker] that links a user's FCM token to their account.
 */
@InternalDataApi
@OptIn(KoinApiExtension::class)
class LinkWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val client by inject<HttpClient>()

    override suspend fun doWork(): Result {
        val token = inputData.getString(KeyToken) ?: return Result.failure()
        return try {
            client.post<Unit>("/notifications") {
                body = NewNotificationTokenDTO(token)
            }
            Result.success()
        } catch (client: ClientRequestException) {
            when (client.response.status) {
                HttpStatusCode.BadRequest -> Result.failure()
                HttpStatusCode.Unauthorized -> Result.retry()
                HttpStatusCode.NotFound -> Result.failure()
                else -> Result.retry()
            }
        } catch (problem: Throwable) {
            Result.retry()
        }
    }

    companion object {

        private const val KeyToken = "FCMToken"

        fun forToken(value: String): Data {
            return workDataOf(KeyToken to value)
        }
    }
}
