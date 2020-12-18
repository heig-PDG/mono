package tupperdate.android.ui.home.recipe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.chrisbanes.accompanist.coil.CoilImageDefaults
import org.koin.androidx.compose.getViewModel
import tupperdate.android.data.legacy.api.ImagePickerApi
import tupperdate.android.data.legacy.api.ImageType

@Composable
fun NewRecipe(
    imagePickerApi: ImagePickerApi,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = getViewModel<NewRecipeViewModel>()

    val placeholder = "https://via.placeholder.com/450"
    val imageUri = remember { imagePickerApi.currentRecipe }.collectAsState(initial = null).value

    val heroImage = if (imageUri == null) {
        // TODO: Remove this, we do not want to invalidate the cache just because we added an image...
        //       or maybe we do
        CoilImageDefaults.defaultImageLoader().memoryCache.clear()
        placeholder
    } else {
        imageUri
    }

    val (recipe, setRecipe) = remember {
        mutableStateOf(
            EditableRecipe(
                title = "",
                description = "",
                vegetarian = true,
                warm = false,
                hasAllergens = true,
            )
        )
    }

    EditRecipe(
        heroImage = heroImage,
        recipe = recipe,
        onRecipeChange = setRecipe,
        onDeleteClick = { onBack() },
        // TODO : Actually put more data in the API.
        onSaveClick = {
            viewModel.onCreateRecipe(
                title = recipe.title,
                description = recipe.description,
                vegetarian = recipe.vegetarian,
                warm = recipe.warm,
                allergenic = recipe.hasAllergens,
                imageUri = imageUri,
            )
            onBack()
        },
        onEdit = { imagePickerApi.pick(ImageType.Recipe) },
        modifier = modifier,
    )
}
