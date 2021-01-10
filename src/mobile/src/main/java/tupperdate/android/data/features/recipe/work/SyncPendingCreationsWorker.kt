package tupperdate.android.data.features.recipe.work

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.readFileAndCompressAsBase64
import tupperdate.android.data.room.TupperdateDatabase
import tupperdate.common.dto.NewRecipeDTO
import tupperdate.common.dto.RecipeAttributesDTO
import tupperdate.common.dto.RecipeDTO

@InternalDataApi
@OptIn(KoinApiExtension::class)
class SyncPendingCreationsWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    companion object {
        private val TAG = SyncPendingCreationsWorker::class.java.simpleName
    }

    private val client: HttpClient by inject()
    private val database: TupperdateDatabase by inject()

    override suspend fun doWork(): Result {
        Log.d(TAG, "Starting Work")
        val creations = database.recipes().pendingCreations().first()
        var result = Result.success()
        for (creation in creations) {
            try {
                val dto = NewRecipeDTO(
                    title = creation.title,
                    description = creation.description,
                    imageBase64 = creation.picture?.toUri()
                        ?.readFileAndCompressAsBase64(applicationContext.contentResolver),
                    attributes = RecipeAttributesDTO(
                        vegetarian = creation.vegetarian,
                        hasAllergens = creation.allergens,
                        warm = creation.warm,
                    )
                )
                client.post<RecipeDTO>("/recipes") {
                    body = dto
                }
                database.recipes().pendingCreationsDelete(creation.localId)
            } catch (throwable: Throwable) {
                // TODO : Look at the result code from the server.
                Log.w(TAG, "Error while creating recipe $creation", throwable)
                result = Result.retry()
            }
        }
        Log.d(TAG, "Finishing work with result $result")
        return result
    }
}
