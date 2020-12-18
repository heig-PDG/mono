package tupperdate.android.ui.home.recipe

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tupperdate.android.data.legacy.api.RecipeApi

class NewRecipeViewModel(
    private val api: RecipeApi,
) : ViewModel() {

    fun onCreateRecipe(
        title: String,
        description: String,
        vegetarian: Boolean,
        warm: Boolean,
        allergenic: Boolean,
        imageUri: Uri?,
    ) {
        viewModelScope.launch {
            api.create(
                title = title,
                description = description,
                vegetarian = vegetarian,
                warm = warm,
                hasAllergens = allergenic,
                imageUri = imageUri,
            )
        }
    }
}
