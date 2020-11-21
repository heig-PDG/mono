package tupperdate.android.editRecipe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LifecycleOwnerAmbient
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tupperdate.api.ImageApi
import tupperdate.api.RecipeApi

@Composable
fun NewRecipe(
    api: RecipeApi,
    imageApi: ImageApi,
    onSaved: () -> Unit,
    onCancelled: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val image = "https://via.placeholder.com/450" // TODO : Handle images.
    val (recipe, setRecipe) = remember {
        mutableStateOf(
            EditableRecipe(
                title = "",
                description = "",
                vegan = true,
                hot = false,
                hasAllergens = true,
            )
        )
    }
    val scope = LifecycleOwnerAmbient.current.lifecycleScope
    EditRecipe(
        heroImageUrl = image,
        recipe = recipe,
        onRecipeChange = setRecipe,
        onDeleteClick = { onCancelled() },
        // TODO : Actually put more data in the API.
        onSaveClick = {
            scope.launch {
                api.create(title = recipe.title, description = recipe.description)
                onSaved()
            }
        },
        imageApi = imageApi,
        modifier = modifier,
    )
}
