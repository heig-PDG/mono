package tupperdate.android.ui.home.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tupperdate.android.data.features.profile.RestrictionsRepository
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.android.data.features.recipe.RecipeRepository

/**
 * A [ViewModel] for the feed screen.
 *
 * @param recipeRepository the [RecipeRepository] that can be used to fetch the recipes in the stack.
 * @param restrictions the [RestrictionsRepository] that acn be used to display restriction warnings.
 */
class FeedViewModel(
    private val recipeRepository: RecipeRepository,
    private val restrictions: RestrictionsRepository,
) : ViewModel() {

    /**
     * The last recipe that was swiped in the stack. It will get replaced as new recipes are liked
     * or disliked, letting you issue a new vote.
     */
    private val lastSwiped = MutableStateFlow<Recipe?>(null)

    private val stack = recipeRepository.stack()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    /**
     * A [Flow] that emits true if the unswipe action should be enabled.
     */
    val unswipeEnabled = lastSwiped.map { it != null }

    /**
     * A [Flow] that emits true if recipes should have warnings if they contain allergens.
     */
    val warnIfHasAllergens: Flow<Boolean> = restrictions.warnIfHasAllergens

    /**
     * A [Flow] that emits true if recipes should have warnings if they're not vegetarian.
     */
    val warnIfNotVegetarian: Flow<Boolean> = restrictions.warnIfNotVegetarian

    /**
     * Returns a [Flow] of the recipes that should be displayed in the stack.
     */
    fun stack(): Flow<List<Recipe>> {
        return stack
    }

    /**
     * Callback called when the user presses the back button.
     */
    fun onUnswipe() {
        val recipe = lastSwiped.value ?: return
        lastSwiped.value = null
        viewModelScope.launch {
            recipeRepository.unswipe(recipe.identifier)
        }
    }

    /**
     * Callback called when the user presses the like button.
     */
    fun onLike() {
        val top = stack.value.firstOrNull() ?: return
        viewModelScope.launch {
            lastSwiped.value = top
            recipeRepository.like(top.identifier)
        }
    }

    /**
     * Callback called when the user pressed the dislike button.
     */
    fun onDislike() {
        val top = stack.value.firstOrNull() ?: return
        viewModelScope.launch {
            lastSwiped.value = top
            recipeRepository.dislike(top.identifier)
        }
    }
}
