package tupperdate.android.ui.home.recipe

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import tupperdate.android.data.features.picker.ImagePicker
import tupperdate.android.data.features.recipe.NewRecipe
import tupperdate.android.data.features.recipe.RecipeRepository

class NewRecipeViewModel(
    private val picker: ImagePicker,
    private val repository: RecipeRepository,
) : ViewModel() {

    private val uri = MutableStateFlow<Uri?>(null)
    private val picture = uri.filterNotNull()

    fun picture(): Flow<Uri> {
        return picture
    }

    fun onPick() {
        viewModelScope.launch {
            val handle = picker.pick()
            if (handle != null) {
                uri.value = handle.uri
            }
        }
    }

    fun onSubmit(recipe: NewRecipe) {
        repository.create(recipe.copy(picture = uri.value))
    }
}
