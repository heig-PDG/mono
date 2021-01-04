package tupperdate.android.ui.home.feed

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.android.data.features.recipe.RecipeRepository

/**
 * A [ViewModel] for the feed screen.
 *
 * @param recipeRepository the [RecipeRepository] that can be used to fetch the recipes in the stack.
 */
class FeedViewModel(
    recipeRepository: RecipeRepository
) : ViewModel() {

    private val stack = recipeRepository.stack()

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
        /* TODO : Implement this.*/
    }

    /**
     * Callback called when the user pressed the dislike button.
     */
    fun onDislike() {
        /* TODO : Implement this.*/
    }
}
