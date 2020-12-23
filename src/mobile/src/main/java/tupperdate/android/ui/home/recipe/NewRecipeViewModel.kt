package tupperdate.android.ui.home.recipe

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tupperdate.android.data.features.recipe.NewRecipe
import tupperdate.android.data.features.recipe.RecipeRepository
import tupperdate.android.data.legacy.api.readFileAsBase64

class NewRecipeViewModel(
    application: Application,
    private val repository: RecipeRepository,
) : AndroidViewModel(application) {

    fun onSubmit(recipe: NewRecipe, image: Uri?) {
        // TODO : Handle image management. Maybe have some dedicated Worker steps ?
        // TODO : Eventually migrate data ownership to ViewModel rather than Compose UI.
        repository.create()
    }
}
