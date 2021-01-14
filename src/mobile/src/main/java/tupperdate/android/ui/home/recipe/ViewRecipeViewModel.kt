package tupperdate.android.ui.home.recipe

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.android.data.features.recipe.RecipeRepository

class ViewRecipeViewModel(
    recipeRepository: RecipeRepository,
    identifier: String,
) : ViewModel() {

    val recipe: Flow<Recipe> = recipeRepository.single(identifier)
        .filterNotNull()
}
