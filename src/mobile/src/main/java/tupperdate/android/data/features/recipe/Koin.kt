package tupperdate.android.data.features.recipe

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.dsl.module
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.recipe.impl.RecipeRepositoryImpl

/**
 * A Koin module that prepares the DAOs, sources of truths and stores necessary to manipulate
 * objects related to the "recipes" feature of Tupperdate.
 */
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
val KoinModuleDataRecipe = module {

    // Stores.
    @OptIn(InternalDataApi::class)
    factory<RecipeRepository> { RecipeRepositoryImpl(get(), get(), get(), get()) }
}
