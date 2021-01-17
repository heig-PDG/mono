package tupperdate.android.data.features.recipe.work

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.readFileAndCompressAsBase64
import tupperdate.android.data.room.TupperdateDatabase
import tupperdate.common.dto.RecipeAttributesPartDTO
import tupperdate.common.dto.RecipePartDTO
import utils.OptionalProperty.NotProvided
import utils.OptionalProperty.Provided

@InternalDataApi
@OptIn(KoinApiExtension::class)
class SyncPendingUpdatesWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val client: HttpClient by inject()
    private val database: TupperdateDatabase by inject()

    override suspend fun doWork(): Result {
        val updates = database.recipes().pendingUpdates().first()
        var result = Result.success()
        for (update in updates) {
            try {
                val dto = RecipePartDTO(
                    title =
                    if (update.titleUpdate) Provided(update.title)
                    else NotProvided,
                    description =
                    if (update.descriptionUpdate) Provided(update.description)
                    else NotProvided,
                    picture =
                    if (update.pictureUpdate) Provided(
                        update.picture
                            ?.toUri()
                            ?.readFileAndCompressAsBase64(applicationContext.contentResolver)
                    )
                    else NotProvided,
                    attributes = RecipeAttributesPartDTO(
                        hasAllergens =
                        if (update.allergensUpdate) Provided(update.allergens)
                        else NotProvided,
                        vegetarian =
                        if (update.vegetarianUpdate) Provided(update.vegetarian)
                        else NotProvided,
                        warm =
                        if (update.warmUpdate) Provided(update.warm)
                        else NotProvided,
                    )
                )
                client.patch<Unit>("/recipes/${update.id}") {
                    body = dto
                }
                database.recipes().pendingUpdatesDelete(update.id)
            } catch (client: ClientRequestException) {
                if (client.response.status == HttpStatusCode.NotFound
                    || client.response.status == HttpStatusCode.Unauthorized
                ) {
                    database.recipes().pendingUpdatesDelete(update.id)
                }
            } catch (throwable: Throwable) {
                Log.w(
                    SyncPendingUpdatesWorker::class.simpleName,
                    "Error while creating recipe $update",
                    throwable
                )
                result = Result.retry()
            }
        }
        return result
    }
}
