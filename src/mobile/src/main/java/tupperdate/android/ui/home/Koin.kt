package tupperdate.android.ui.home

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tupperdate.android.data.features.picker.ImagePicker
import tupperdate.android.ui.home.feed.FeedViewModel
import tupperdate.android.ui.home.profile.ProfileViewModel
import tupperdate.android.ui.home.recipe.NewRecipeViewModel
import tupperdate.android.ui.home.recipe.ViewRecipeViewModel

val KoinModuleUIHome = module {
    viewModel { HomeViewModel() }
    viewModel { FeedViewModel(get()) }
    viewModel { (picker: ImagePicker) ->
        ProfileViewModel(get(), get())
    }
    viewModel { (picker: ImagePicker, onBack: () -> Unit) ->
        NewRecipeViewModel(picker, onBack, get())
    }
    viewModel { (recipeId: String) -> ViewRecipeViewModel(get(), recipeId) }
}
