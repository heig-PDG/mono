package tupperdate.android.ui.home

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import tupperdate.android.data.features.picker.ImagePicker
import tupperdate.android.ui.home.feed.FeedViewModel
import tupperdate.android.ui.home.profile.ProfileViewModel
import tupperdate.android.ui.home.recipe.NewRecipeViewModel
import tupperdate.android.ui.home.recipe.ViewRecipeViewModel

val KoinHomeModule = module {
    viewModel { FeedViewModel(get()) }
    viewModel { (picker: ImagePicker) -> ProfileViewModel(get(parameters = { parametersOf(picker) })) }
    viewModel { (picker: ImagePicker) -> NewRecipeViewModel(picker, get()) }
    viewModel { (recipeId: String) -> ViewRecipeViewModel(get(), recipeId) }
}
