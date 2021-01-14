package tupperdate.android.ui.home.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tupperdate.android.R
import tupperdate.android.ui.theme.TupperdateTheme

/**
 * Displays a horizontal list of [RecipeInfoButton], with different properties.
 *
 * @param vegan true if the food is vegan
 * @param hot true if the food is warm
 * @param hasAllergens true if the food has allergens
 * @param modifier the [Modifier] of the composable
 * @param onClickVegan a callback for when the vegan icon is clicked
 * @param onClickHot a callback for when the hot icon is clicked
 * @param onClickAllergens a callback for when the allergens icon is clicked
 */
@Composable
fun RecipeTags(
    vegan: Boolean,
    hot: Boolean,
    hasAllergens: Boolean,
    modifier: Modifier = Modifier,
    onClickVegan: (() -> Unit)? = NoOp,
    onClickHot: (() -> Unit)? = NoOp,
    onClickAllergens: (() -> Unit)? = NoOp,
) {
    Row(
        modifier.wrapContentWidth(),
        Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        Alignment.CenterVertically,
    ) {
        RecipeInfoButton(
            onClick = onClickVegan ?: NoOp,
            icon = vectorResource(if (vegan) R.drawable.ic_editrecipe_veggie else R.drawable.ic_editrecipe_not_veggie),
            title = (if (vegan) stringResource(R.string.edit_recipe_veggie)
            else stringResource(R.string.edit_recipe_not_veggie)),
            modifier = Modifier.weight(1f),
            enabled = onClickVegan != null,
        )
        VerticalDivider(Modifier.preferredHeight(48.dp))
        RecipeInfoButton(
            onClick = onClickHot ?: NoOp,
            icon = vectorResource(if (hot) R.drawable.ic_editrecipe_warm else R.drawable.ic_editrecipe_cold),
            title = (if (hot) stringResource(R.string.edit_recipe_warm)
            else stringResource(R.string.edit_recipe_cold)),
            modifier = Modifier.weight(1f),
            enabled = onClickHot != null,
        )
        VerticalDivider(Modifier.preferredHeight(48.dp))
        RecipeInfoButton(
            onClick = onClickAllergens ?: NoOp,
            icon = vectorResource(if (hasAllergens) R.drawable.ic_editrecipe_allergens else R.drawable.ic_editrecipe_allergens),
            title = (if (hasAllergens) stringResource(id = R.string.edit_recipe_allergens)
            else stringResource(id = R.string.edit_recipe_no_allergens)),
            modifier = Modifier.weight(1f),
            enabled = onClickAllergens != null,
        )
    }
}

// Avoids a new allocation on each recomposition.
private val NoOp = {}

/**
 * Taken from [androidx.compose.material.Divider].
 */
@Composable
private fun VerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onSurface.copy(alpha = DividerAlpha),
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp
) {
    val indentMod = if (startIndent.value != 0f) {
        Modifier.padding(top = startIndent)
    } else {
        Modifier
    }
    Box(
        modifier.then(indentMod)
            .fillMaxHeight()
            .preferredWidth(thickness)
            .background(color = color)
    )
}

private const val DividerAlpha = 0.12f

// PREVIEWS

@Preview(showBackground = true)
@Composable
private fun RecipeTagsPreview() {
    TupperdateTheme {
        val (veg, setVeg) = remember { mutableStateOf(false) }
        val (hot, setHot) = remember { mutableStateOf(false) }
        val (all, setAll) = remember { mutableStateOf(false) }

        RecipeTags(
            vegan = veg,
            hot = hot,
            hasAllergens = all,
            onClickVegan = { setVeg(!veg) },
            onClickHot = { setHot(!hot) },
            onClickAllergens = { setAll(!all) },
        )
    }
}
