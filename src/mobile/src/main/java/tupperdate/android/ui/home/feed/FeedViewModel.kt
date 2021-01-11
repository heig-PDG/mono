package tupperdate.android.ui.home.feed

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
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
    private val dropped = MutableStateFlow(0)

    /**
     * Returns a [Flow] of the recipes that should be displayed in the stack.
     */
    fun stack(): Flow<List<Recipe>> {
        return combine(stack, dropped) { s, count -> s.drop(count) }
    }

    /**
     * Callback called when the user presses the like button.
     */
    fun onLike() {
        // TODO : Actually use the API.
        dropped.value = dropped.value + 1
    }

    /**
     * Callback called when the user pressed the dislike button.
     */
    fun onDislike() {
        // TODO : Actually use the API.
        dropped.value = dropped.value + 1
    }
}
