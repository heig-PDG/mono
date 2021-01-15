package tupperdate.android.ui.home.recipe

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import tupperdate.android.data.features.recipe.NewRecipe
import tupperdate.android.ui.ambients.AmbientImagePicker
import tupperdate.android.ui.theme.PlaceholderRecipeImage

@Composable
fun NewRecipe(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val picker = AmbientImagePicker.current
    val viewModel = getViewModel<NewRecipeViewModel> { parametersOf(picker, onBack) }
    val heroImage by viewModel.picture().collectAsState(PlaceholderRecipeImage)

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
        onSaveClick = {
            viewModel.onSubmit(
                NewRecipe(
                    title = recipe.title,
                    description = recipe.description,
                    isVegan = recipe.vegetarian,
                    isWarm = recipe.warm,
                    hasAllergens = recipe.hasAllergens,
                    picture = null, // Images are added internally by the ViewModel.
                )
            )
        },
        onEdit = viewModel::onPick,
        modifier = modifier,
    )
}
