package tupperdate.android.data.features.recipe.work

import android.content.Context
import android.util.Log
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

@InternalDataApi
@OptIn(KoinApiExtension::class)
class SyncPendingVotesWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    companion object {
        private val TAG = SyncPendingVotesWorker::class.java.simpleName
    }

    private val client: HttpClient by inject()
    private val database: TupperdateDatabase by inject()

    override suspend fun doWork(): Result {
        Log.d(TAG, "Starting Work")
        val votes = database.recipes().pendingRatings().first()
        var result = Result.success()
        for (vote in votes) {
            try {
                with(vote) {
                    val method = if (like) "like" else "dislike"
                    val url = "/recipes/${identifier}/${method}"
                    client.put<Unit>(url)
                    database.recipes().pendingRatingsDelete(identifier)
                }
            } catch (throwable: Throwable) {
                // TODO : Look at the result code from the server.
                Log.w(TAG, "Error while publishing vote $vote", throwable)
                result = Result.retry()
            }
        }
        Log.d(TAG, "Finishing work with result $result")
        return result
    }
}
