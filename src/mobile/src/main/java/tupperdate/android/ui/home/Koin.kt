package tupperdate.android.ui.home

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tupperdate.android.ui.home.feed.FeedViewModel
import tupperdate.android.ui.home.recipe.NewRecipeViewModel

val KoinHomeModule = module {
    viewModel { FeedViewModel(get()) }
    viewModel { NewRecipeViewModel(get(), get()) }
}
