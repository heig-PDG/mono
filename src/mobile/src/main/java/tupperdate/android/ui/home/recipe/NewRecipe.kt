package tupperdate.android.ui.home.recipe

import android.net.Uri
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import tupperdate.android.ui.ambients.AmbientImagePicker

@Composable
fun NewRecipe(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val picker = AmbientImagePicker.current
    val viewModel = getViewModel<NewRecipeViewModel> { parametersOf(picker) }

    val defaultImage = remember { Uri.parse("https://via.placeholder.com/450") }
    val heroImage by viewModel.picture().collectAsState(defaultImage)

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
                tupperdate.android.data.features.recipe.NewRecipe(
                    title = recipe.title,
                    description = recipe.description,
                    isVegan = recipe.vegetarian,
                    isWarm = recipe.warm,
                    hasAllergens = recipe.hasAllergens,
                    picture = null, // TODO : Handle images.
                )
            )
            onBack()
        },
        onEdit = viewModel::onPick,
        modifier = modifier,
    )
}
