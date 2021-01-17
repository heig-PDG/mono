package tupperdate.android.ui.home.recipe

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import tupperdate.android.data.features.picker.ImagePicker
import tupperdate.android.data.features.recipe.RecipeRepository
import tupperdate.android.data.features.recipe.UpdateRecipe

class UpdateRecipeViewModel(
    identifier: String,
    private val picker: ImagePicker,
    private val recipes: RecipeRepository,
) : ViewModel() {

    private val recipe = recipes.single(identifier)

    /**
     * The recipe that we are currently editing. By default, no fields will be updated, but when
     * submitted, the object will be passed to the repository for further processing.
     */
    private val editing = MutableStateFlow(
        UpdateRecipe(
            id = identifier,
            title = "",
            titleUpdate = false,
            description = "",
            descriptionUpdate = false,
            picture = null,
            pictureUpdate = false,
            hasAllergens = false,
            hasAllergensUpdate = false,
            isWarm = false,
            isWarmUpdate = false,
            isVegan = false,
            isVeganUpdate = false,
        )
    )

    val title = combine(recipe, editing) { existing, new ->
        if (new.titleUpdate) new.title
        else existing.title
    }

    val description = combine(recipe, editing) { existing, new ->
        if (new.descriptionUpdate) new.description
        else existing.description
    }

    val picture = combine(recipe, editing) { existing, new ->
        if (new.pictureUpdate) new.picture
        else existing.picture?.toUri()
    }

    val hasAllergens = combine(recipe, editing) { existing, new ->
        if (new.hasAllergensUpdate) new.hasAllergens
        else existing.attributes.hasAllergens
    }

    val isWarm = combine(recipe, editing) { existing, new ->
        if (new.isWarmUpdate) new.isWarm
        else existing.attributes.warm
    }

    val isVegetarian = combine(recipe, editing) { existing, new ->
        if (new.isVeganUpdate) new.isVegan
        else existing.attributes.vegetarian
    }

    fun onTitleChanged(title: String) {
        editing.value = editing.value.copy(title = title, titleUpdate = true)
    }

    fun onDescriptionChanged(description: String) {
        editing.value = editing.value.copy(description = description, descriptionUpdate = true)
    }

    fun onPictureClick() {
        viewModelScope.launch {
            val picture = picker.pick()
            editing.value = editing.value.copy(picture = picture?.uri, pictureUpdate = true)
        }
    }

    fun onAllergensChanged(value: Boolean) {
        editing.value = editing.value.copy(hasAllergens = value, hasAllergensUpdate = true)
    }

    fun onWarmChanged(value: Boolean) {
        editing.value = editing.value.copy(isWarm = value, isWarmUpdate = true)
    }

    fun onVegetarianChanged(value: Boolean) {
        editing.value = editing.value.copy(isVegan = value, isVeganUpdate = true)
    }

    fun onSubmit() {
        viewModelScope.launch {
            recipes.update(editing.value)
        }
    }
}
