package tupperdate.android.ui.home.recipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tupperdate.android.R
import tupperdate.android.ui.theme.material.BrandedButton

/**
 * A data class representing the different fields that can be edited in a recipe.
 */
data class EditableRecipe(
    val title: String,
    val description: String,
    val vegetarian: Boolean,
    val warm: Boolean,
    val hasAllergens: Boolean,
)

/**
 * A composable that displays some editable fields for recipes.
 *
 * @param heroImage the image to display. Can be a drawable, URI, URL, ...
 * @param recipe the [EditableRecipe] to display.
 * @param onRecipeChange a callback called when the recipe is changed.
 * @param onDeleteClick a callback called when the delete button is pressed.
 * @param onSaveClick a callback called when the save button is pressed.
 * @param modifier the [Modifier] for this composable.
 */
@Composable
fun EditRecipe(
    heroImage: Any,
    recipe: EditableRecipe,
    onRecipeChange: (EditableRecipe) -> Unit,
    onDeleteClick: () -> Unit,
    onSaveClick: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    RecipeDetail(
        heroImage = heroImage,
        header = {
            EditRecipeHeader(
                title = recipe.title,
                onTitleChange = { onRecipeChange(recipe.copy(title = it)) },
                onDeleteClick = onDeleteClick,
                onSaveClick = onSaveClick,
            )
        },
        icons = {
            RecipeTags(
                vegan = recipe.vegetarian,
                hot = recipe.warm,
                hasAllergens = recipe.hasAllergens,
                onClickVegan = { onRecipeChange(recipe.copy(vegetarian = !recipe.vegetarian)) },
                onClickHot = { onRecipeChange(recipe.copy(warm = !recipe.warm)) },
                onClickAllergens = { onRecipeChange(recipe.copy(hasAllergens = !recipe.hasAllergens)) }
            )
        },
        description = {
            OutlinedTextField(
                value = recipe.description,
                onValueChange = { onRecipeChange(recipe.copy(description = it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.edit_recipe_label_description)) },
                placeholder = { Text(stringResource(id = R.string.edit_recipe_placeholder_description)) },
            )
        },
        onClose = onDeleteClick,
        onEdit = onEdit,
        modifier = modifier,
    )
}

/**
 * The header of the recipe information. Displays the title input, as well as different navigation
 * options.
 */
@Composable
private fun EditRecipeHeader(
    title: String,
    onTitleChange: (String) -> Unit,
    onDeleteClick: () -> Unit,
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
            value = stringResource(id = R.string.edit_recipe_delete),
            onClick = onDeleteClick,
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
