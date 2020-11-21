package tupperdate.android.editRecipe

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import tupperdate.android.R
import tupperdate.android.ui.TupperdateTheme
import tupperdate.android.ui.material.BrandedButton

/**
 * A data class representing the different fields that can be edited in a recipe.
 */
data class EditableRecipe(
    val title: String,
    val description: String,
    val vegan: Boolean,
    val hot: Boolean,
    val hasAllergens: Boolean,
)

/**
 * A composable that displays some editable fields for recipes.
 *
 * @param heroImageUrl the URL at which the image should be fetched.
 * @param recipe the [EditableRecipe] to display.
 * @param onRecipeChange a callback called when the recipe is changed.
 * @param onDeleteClick a callback called when the delete button is pressed.
 * @param onSaveClick a callback called when the save button is pressed.
 * @param modifier the [Modifier] for this composable.
 */
@Composable
fun EditRecipe(
    heroImageUrl: String,
    recipe: EditableRecipe,
    onRecipeChange: (EditableRecipe) -> Unit,
    onDeleteClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    RecipeDetail(
        heroImage = heroImageUrl,
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
                vegan = recipe.vegan,
                hot = recipe.hot,
                hasAllergens = recipe.hasAllergens,
                onClickVegan = { onRecipeChange(recipe.copy(vegan = !recipe.vegan)) },
                onClickHot = { onRecipeChange(recipe.copy(hot = !recipe.hot)) },
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
        modifier = modifier,
        onEdit = { /* TODO : Support image changes. */ },
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

// PREVIEWS

@Preview(showBackground = true)
@Composable
private fun EditRecipePreview() {
    TupperdateTheme {
        val (recipe, setRecipe) = remember {
            mutableStateOf(
                EditableRecipe(
                    title = "Chilli con carne",
                    description = "A wonderful meat-based meal with whatever something.",
                    vegan = false,
                    hot = true,
                    hasAllergens = false,
                )
            )
        }
        EditRecipe(
            heroImageUrl = "",
            recipe = recipe,
            onRecipeChange = setRecipe,
            onDeleteClick = {},
            onSaveClick = {},
        )
    }
}
