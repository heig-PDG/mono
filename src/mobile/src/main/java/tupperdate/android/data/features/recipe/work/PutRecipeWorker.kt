package tupperdate.android.data.features.recipe.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import io.ktor.client.*
import io.ktor.client.request.*
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.room.TupperdateDatabase

@InternalDataApi
@OptIn(KoinApiExtension::class)
class PutRecipeWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val client: HttpClient by inject()
    private val database: TupperdateDatabase by inject()

    override suspend fun doWork(): Result {
        val id = inputData.getString(KeyId) ?: return Result.failure()
        val method = inputData.getString(KeyAction) ?: return Result.failure()
        return try {
            client.put<Unit>("/recipes/${id}/${method}")
            // TODO : eventually keep recipes cached locally, but mark them as liked
            database.recipes().recipesDelete(id)
            Result.success()
        } catch (any: Throwable) {
            println("Put failed")
            any.printStackTrace()
            Result.retry()
        }
    }

    companion object {

        private const val KeyId = "id"
        private const val KeyAction = "action"

        private const val ActionLike = "like"
        private const val ActionDislike = "dislike"

        fun like(id: String): Data {
            return workDataOf(
                KeyId to id,
                KeyAction to ActionLike,
            )
        }

        fun dislike(id: String): Data {
            return workDataOf(
                KeyId to id,
                KeyAction to ActionDislike,
            )
        }
    }
}
