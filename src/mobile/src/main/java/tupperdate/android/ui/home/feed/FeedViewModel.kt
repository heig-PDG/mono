package tupperdate.android.ui.home.feed

import androidx.lifecycle.ViewModel
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import tupperdate.android.data.features.recipe.Recipe

/**
 * A [ViewModel] for the feed screen.
 *
 * @param recipeStackStore the [Store] that can be used to fetch the recipes in the stack.
 */
class FeedViewModel(
    recipeStackStore: Store<Unit, List<Recipe>>,
) : ViewModel() {

    private val stack = recipeStackStore
        .stream(StoreRequest.cached(Unit, true))
        .mapNotNull { it.dataOrNull() }

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
