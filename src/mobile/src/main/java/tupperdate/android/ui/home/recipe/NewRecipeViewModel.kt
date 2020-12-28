package tupperdate.android.ui.home.recipe

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import tupperdate.android.data.features.picker.ImagePicker
import tupperdate.android.data.features.recipe.NewRecipe
import tupperdate.android.data.features.recipe.RecipeRepository

class NewRecipeViewModel(
    application: Application,
    private val repository: RecipeRepository,
    private val picker: ImagePicker,
) : AndroidViewModel(application) {

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
