package tupperdate.android.ui.home.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import tupperdate.android.data.features.recipe.RecipeRepository

class ViewRecipeViewModel(
    recipeRepository: RecipeRepository,
    identifier: String,
) : ViewModel() {

    private val recipe = recipeRepository.single(identifier)

    private val title = recipe
        .map { it.title }
        .stateIn(viewModelScope, Eagerly, "")

    private val description = recipe
        .map { it.description }
        .stateIn(viewModelScope, Eagerly, "")

    private val picture = recipe
        .map { it.picture }
        .onEach { println("Found $it") }
        .stateIn(viewModelScope, Eagerly, "")

    fun title(): StateFlow<String> = title

    fun description(): StateFlow<String> = description

    fun picture(): StateFlow<String?> = picture

}
