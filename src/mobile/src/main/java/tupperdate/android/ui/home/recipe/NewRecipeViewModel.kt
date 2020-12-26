package tupperdate.android.ui.home.recipe

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import tupperdate.android.data.features.recipe.NewRecipe
import tupperdate.android.data.features.recipe.RecipeRepository

class NewRecipeViewModel(
    application: Application,
    private val repository: RecipeRepository,
) : AndroidViewModel(application) {

    fun onSubmit(recipe: NewRecipe, image: Uri?) {
        // TODO : Handle image management. Maybe have some dedicated Worker steps ?
        // TODO : Eventually migrate data ownership to ViewModel rather than Compose UI.
        repository.create(recipe)
    }
}
