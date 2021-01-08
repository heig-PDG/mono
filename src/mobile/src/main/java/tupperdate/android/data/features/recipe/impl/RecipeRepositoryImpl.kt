package tupperdate.android.data.features.recipe.impl

import androidx.work.*
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import io.ktor.client.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.recipe.NewRecipe
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.android.data.features.recipe.RecipeRepository
import tupperdate.android.data.features.recipe.api.RecipeFetchers
import tupperdate.android.data.features.recipe.room.RecipeSourceOfTruth
import tupperdate.android.data.features.recipe.room.RecipeStackSourceOfTruth
import tupperdate.android.data.features.recipe.work.NewRecipeWorker
import tupperdate.android.data.room.TupperdateDatabase
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@InternalDataApi
class RecipeRepositoryImpl(
    private val database: TupperdateDatabase,
    private val client: HttpClient,
    private val workManager: WorkManager,
) : RecipeRepository {

    override fun create(recipe: NewRecipe) {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<NewRecipeWorker>()
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS,
            )
            .setConstraints(constraints)
            .setInputData(NewRecipeWorker.inputData(recipe))
            .build()

        workManager.enqueue(request)
    }

    override fun single(id: String): Flow<Recipe> {
        val store = StoreBuilder.from(
            RecipeFetchers.singleRecipeFetcher(client),
            RecipeSourceOfTruth(database.recipes()),
        ).build()

        return store.stream(StoreRequest.cached(id, true))
            .mapNotNull { it.dataOrNull() }
    }

    override fun stack(): Flow<List<Recipe>> {
        val store = StoreBuilder.from(
            RecipeFetchers.allRecipesFetcher(client),
            RecipeStackSourceOfTruth(database.recipes()),
        ).build()

        return store.stream(StoreRequest.cached(Unit, true))
            .mapNotNull { it.dataOrNull() }
    }
}
