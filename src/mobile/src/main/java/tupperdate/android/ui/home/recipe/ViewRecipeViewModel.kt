package tupperdate.android.ui.home.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.android.data.features.recipe.RecipeRepository

class ViewRecipeViewModel(
    private val recipeRepository: RecipeRepository,
    identifier: String,
) : ViewModel() {

    private val current = recipeRepository.single(identifier)
        .filterNotNull()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val recipe: Flow<Recipe> = current.filterNotNull()

    fun onLike() {
        viewModelScope.launch {
            current.value?.let { recipeRepository.like(it.identifier) }
        }
    }

    fun onDislike() {
        viewModelScope.launch {
            current.value?.let { recipeRepository.dislike(it.identifier) }
        }
    }
}
