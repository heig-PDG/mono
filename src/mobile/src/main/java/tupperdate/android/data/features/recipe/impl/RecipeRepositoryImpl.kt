package tupperdate.android.data.features.recipe.impl

import androidx.work.WorkManager
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import io.ktor.client.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.SyncRequestBuilder
import tupperdate.android.data.features.recipe.NewRecipe
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.android.data.features.recipe.RecipeRepository
import tupperdate.android.data.features.recipe.api.RecipeFetchers
import tupperdate.android.data.features.recipe.room.PendingNewRecipeEntity
import tupperdate.android.data.features.recipe.room.RecipeSourceOfTruth
import tupperdate.android.data.features.recipe.room.RecipeStackSourceOfTruth
import tupperdate.android.data.features.recipe.work.RefreshStackWorker
import tupperdate.android.data.features.recipe.work.SyncPendingCreationsWorker
import tupperdate.android.data.features.recipe.work.SyncPendingVotesWorker
import tupperdate.android.data.room.TupperdateDatabase

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@InternalDataApi
class RecipeRepositoryImpl(
    private val database: TupperdateDatabase,
    private val client: HttpClient,
    private val workManager: WorkManager,
) : RecipeRepository {

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

    override suspend fun create(recipe: NewRecipe) {
        database.recipes().pendingRecipesInsert(
            PendingNewRecipeEntity(
                title = recipe.title,
                description = recipe.description,
                picture = recipe.picture?.toString(),
                warm = recipe.isWarm,
                vegetarian = recipe.isVegan,
                allergens = recipe.hasAllergens,
            )
        )

        // Fetch the newly created recipe.
        workManager.beginWith(SyncRequestBuilder<SyncPendingCreationsWorker>().build())
            .then(SyncRequestBuilder<RefreshStackWorker>().build())
            .enqueue()
    }

    override suspend fun like(id: String) {
        database.recipes().recipesLike(id)
        workManager.beginWith(SyncRequestBuilder<SyncPendingVotesWorker>().build())
            .then(SyncRequestBuilder<RefreshStackWorker>().build())
            .enqueue()
    }

    override suspend fun dislike(id: String) {
        database.recipes().recipesDislike(id)
        workManager.beginWith(SyncRequestBuilder<SyncPendingVotesWorker>().build())
            .then(SyncRequestBuilder<RefreshStackWorker>().build())
            .enqueue()
    }
}
