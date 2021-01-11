package tupperdate.android.ui.home.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.android.data.features.recipe.RecipeRepository

/**
 * A [ViewModel] for the feed screen.
 *
 * @param recipeRepository the [RecipeRepository] that can be used to fetch the recipes in the stack.
 */
class FeedViewModel(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val stack = recipeRepository.stack()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    /**
     * Returns a [Flow] of the recipes that should be displayed in the stack.
     */
    fun stack(): Flow<List<Recipe>> {
        return stack
    }

    /**
     * Callback called when the user presses the like button.
     */
    fun onLike() {
        val top = stack.value.firstOrNull() ?: return
        viewModelScope.launch {
            recipeRepository.like(top.identifier)
        }
    }

    /**
     * Callback called when the user pressed the dislike button.
     */
    fun onDislike() {
        val top = stack.value.firstOrNull() ?: return
        viewModelScope.launch {
            recipeRepository.dislike(top.identifier)
        }
    }
}
