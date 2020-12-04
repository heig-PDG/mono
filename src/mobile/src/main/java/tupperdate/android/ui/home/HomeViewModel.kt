package tupperdate.android.ui.home

import androidx.lifecycle.ViewModel
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import tupperdate.android.data.Graph
import tupperdate.android.data.Recipe

class HomeViewModel(
    recipeStackStore: Store<Unit, List<Recipe>> = Graph.recipeStackStore,
) : ViewModel() {

    private val stack = recipeStackStore
        .stream(StoreRequest.cached(Unit, true))
        .mapNotNull { it.dataOrNull() }

    fun recipes(): Flow<List<Recipe>> {
        return stack
    }
}
