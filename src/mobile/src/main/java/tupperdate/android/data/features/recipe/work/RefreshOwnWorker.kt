package tupperdate.android.data.features.recipe.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.fresh
import io.ktor.client.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.AuthenticationRepository
import tupperdate.android.data.features.recipe.api.RecipeFetchers
import tupperdate.android.data.features.recipe.room.RecipeOwnSourceOfTruth
import tupperdate.android.data.room.TupperdateDatabase

/**
 * A [CoroutineWorker] that can fetch some recipes for the currently logged in user.
 */
@InternalDataApi
@OptIn(
    KoinApiExtension::class,
    ExperimentalCoroutinesApi::class,
    FlowPreview::class,
)
class RefreshOwnWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val auth by inject<AuthenticationRepository>()
    private val client by inject<HttpClient>()
    private val database by inject<TupperdateDatabase>()

    override suspend fun doWork(): Result {
        val store = StoreBuilder.from(
            RecipeFetchers.ownRecipesFetcher(client),
            RecipeOwnSourceOfTruth(auth, database.recipes()),
        ).build()
        store.fresh(Unit)
        return Result.success()
    }
}
