package tupperdate.android.data.features.recipe

import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.dsl.bind
import org.koin.dsl.module
import tupperdate.android.data.features.recipe.room.RecipeSourceOfTruth
import tupperdate.android.data.features.recipe.room.RecipeStackSourceOfTruth
import tupperdate.android.data.room.TupperdateDatabase

/**
 * A Koin module that prepares the DAOs, sources of truths and stores necessary to manipulate
 * objects related to the "recipes" feature of Tupperdate.
 */
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
val KoinRecipeModule = module {
    // DAOs.
    single { get<TupperdateDatabase>().recipes() }
    single { get<TupperdateDatabase>().newRecipes() }

    // SourceOfTruth
    factory { RecipeSourceOfTruth(get()) }
    factory { RecipeStackSourceOfTruth(get()) }

    // Pushers.
    single { NewRecipePusher(get(), get()) }

    // Stores.
    factory {
        StoreBuilder.from(
            RecipeFetcher.allRecipesFetcher(get()),
            RecipeStackSourceOfTruth(get()),
        ).scope(get()).build()
    } bind Store::class // FIXME : Handle generics better.
}
