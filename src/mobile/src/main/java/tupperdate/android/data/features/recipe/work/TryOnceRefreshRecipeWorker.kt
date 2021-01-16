package tupperdate.android.data.features.recipe.work

import android.content.Context
import androidx.work.CoroutineWorker
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
import tupperdate.android.data.features.recipe.api.RecipeFetchers
import tupperdate.android.data.features.recipe.room.RecipeSourceOfTruth
import tupperdate.android.data.room.TupperdateDatabase

/**
 * A [CoroutineWorker] that syncs a single recipe, but does not retry if the sync failed.
 */
@InternalDataApi
@OptIn(
    KoinApiExtension::class,
    ExperimentalCoroutinesApi::class,
    FlowPreview::class,
)
class TryOnceRefreshRecipeWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val client by inject<HttpClient>()
    private val database by inject<TupperdateDatabase>()

    override suspend fun doWork(): Result {
        val id = inputData.getString(KeyRecipeId) ?: return Result.failure()
        val store = StoreBuilder.from(
            fetcher = RecipeFetchers.singleRecipeFetcher(client),
            sourceOfTruth = RecipeSourceOfTruth(database.recipes()),
        ).build()
        try {
            store.fresh(id)
        } finally {
            return Result.success()
        }
    }

    companion object {

        private const val KeyRecipeId = "recipeId"

        fun forId(id: String) = workDataOf(KeyRecipeId to id)
    }
}
