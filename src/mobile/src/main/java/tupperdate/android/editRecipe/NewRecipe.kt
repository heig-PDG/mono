package tupperdate.android.editRecipe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.LifecycleOwnerAmbient
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tupperdate.api.ImageApi
import tupperdate.api.RecipeApi

@Composable
fun NewRecipe(
    recipeApi: RecipeApi,
    imageApi: ImageApi,
    onSaved: () -> Unit,
    onCancelled: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = LifecycleOwnerAmbient.current.lifecycleScope

    val placeholder = "https://via.placeholder.com/450"
    val image = remember { imageApi.read() }.collectAsState(initial = null).value

    val heroImage = image?.toDrawable(ContextAmbient.current.resources) ?: placeholder

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

    EditRecipe(
        heroImage = heroImage,
        recipe = recipe,
        onRecipeChange = setRecipe,
        onDeleteClick = { onCancelled() },
        // TODO : Actually put more data in the API.
        onSaveClick = {
            scope.launch {
                recipeApi.create(title = recipe.title, description = recipe.description)
                onSaved()
            }
        },
        onEdit = { imageApi.launch() },
        modifier = modifier,
    )
}
