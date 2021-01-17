package tupperdate.android.ui.home.recipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import tupperdate.android.R
import tupperdate.android.ui.ambients.AmbientImagePicker
import tupperdate.android.ui.theme.PlaceholderRecipeImage
import tupperdate.android.ui.theme.material.BrandedButton

/**
 * A composable that displays some editable fields for existing recipes.
 *
 * @param identifier the [String] identifier for the recipe to display.
 * @param onBack a callback called when this recipe should be closed.
 * @param modifier the [Modifier] for this composable.
 */
@Composable
fun UpdateRecipe(
    identifier: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val picker = AmbientImagePicker.current
    val viewModel = getViewModel<UpdateRecipeViewModel> {
        parametersOf(identifier, onBack, picker)
    }
    val heroImage by viewModel.picture.collectAsState(null)
    val title by viewModel.title.collectAsState("")
    val description by viewModel.description.collectAsState("")
    val vegetarian by viewModel.isVegetarian.collectAsState(false)
    val allergens by viewModel.hasAllergens.collectAsState(false)
    val warm by viewModel.isWarm.collectAsState(false)

    RecipeDetail(
        heroImage = heroImage ?: PlaceholderRecipeImage,
        header = {
            UpdateRecipeHeader(
                title = title,
                onTitleChange = viewModel::onTitleChanged,
                onCancelClick = onBack,
                onSaveClick = viewModel::onSubmit,
            )
        },
        icons = {
            RecipeTags(
                vegan = vegetarian,
                hot = warm,
                hasAllergens = allergens,
                onClickVegan = { viewModel.onVegetarianChanged(!vegetarian) },
                onClickHot = { viewModel.onWarmChanged(!warm) },
                onClickAllergens = { viewModel.onAllergensChanged(!allergens) }
            )
        },
        description = {
            OutlinedTextField(
                value = description,
                onValueChange = viewModel::onDescriptionChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.edit_recipe_label_description)) },
                placeholder = { Text(stringResource(id = R.string.edit_recipe_placeholder_description)) },
            )
        },
        onClose = onBack,
        onEdit = viewModel::onPictureClick,
        modifier = modifier,
    )
}

/**
 * The header of the recipe information. Displays the title input, as well as different navigation
 * options.
 */
@Composable
private fun UpdateRecipeHeader(
    title: String,
    onTitleChange: (String) -> Unit,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = title,
        onValueChange = onTitleChange,
        label = { Text(stringResource(id = R.string.edit_recipe_label_title)) },
        placeholder = { Text(stringResource(id = R.string.edit_recipe_placeholder_title)) },
    )
    Row(
        modifier.fillMaxWidth(),
        Arrangement.spacedBy(8.dp),
        Alignment.CenterVertically,
    ) {
        BrandedButton(
            value = stringResource(id = R.string.edit_recipe_cancel),
            onClick = onCancelClick,
            modifier = modifier.weight(1f, fill = true),
            shape = RoundedCornerShape(8.dp)
        )
        BrandedButton(
            value = stringResource(id = R.string.edit_recipe_save),
            onClick = onSaveClick,
            modifier = modifier.weight(1f, fill = true),
            shape = RoundedCornerShape(8.dp)
        )
    }
}
