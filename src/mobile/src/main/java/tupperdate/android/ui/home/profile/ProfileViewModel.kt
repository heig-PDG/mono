package tupperdate.android.ui.home.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import tupperdate.android.data.features.auth.AuthenticationRepository
import tupperdate.android.data.features.picker.ImagePicker
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.android.data.features.recipe.RecipeRepository

class ProfileViewModel(
    private val picker: ImagePicker,
    private val auth: AuthenticationRepository,
    private val recipe: RecipeRepository,
) : ViewModel() {

    private val currentlyEditing = MutableStateFlow(false)

    val editing: Flow<Boolean> = currentlyEditing
    val recipes: Flow<List<Recipe>> = recipe.own()

    fun onEditClick() {
        currentlyEditing.value = true
    }

    fun onProfilePictureClick() {
        viewModelScope.launch {
            auth.updateProfile(picture = picker.pick())
        }
    }

    fun onSave(
        name: String,
    ) {
        viewModelScope.launch {
            auth.updateProfile(displayName = name)
            currentlyEditing.value = false
        }
    }
}
