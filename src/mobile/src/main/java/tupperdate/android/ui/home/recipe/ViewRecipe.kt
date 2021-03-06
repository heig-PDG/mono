package tupperdate.android.ui.home.recipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import tupperdate.android.R
import tupperdate.android.ui.theme.DislikeButton
import tupperdate.android.ui.theme.LikeButton
import tupperdate.android.ui.theme.PlaceholderRecipeImage

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ViewRecipe(
    identifier: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = getViewModel<ViewRecipeViewModel> { parametersOf(identifier) }
    val recipe by viewModel.recipe.collectAsState(null)

    RecipeDetail(
        heroImage = recipe?.picture ?: PlaceholderRecipeImage,
        header = {
            Text(
                text = stringResource(id = R.string.edit_recipe_title),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.overline
            )
            Text(
                text = recipe?.title ?: "",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.h5
            )
            ViewRecipeButtons(
                onSkip = {
                    viewModel.onDislike()
                    onBack()
                },
                onLike = {
                    viewModel.onLike()
                    onBack()
                },
            )
        },
        icons = {
            RecipeTags(
                vegan = recipe?.attributes?.vegetarian ?: false,
                hot = recipe?.attributes?.warm ?: false,
                hasAllergens = recipe?.attributes?.hasAllergens ?: false,
            )
        },
        description = {
            Text(
                text = stringResource(id = R.string.edit_recipe_description),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.overline
            )
            Text(
                text = recipe?.description ?: "",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.body1,
            )
        },
        onClose = onBack,
        modifier = modifier,
    )
}

@Composable
private fun ViewRecipeButtons(
    onSkip: () -> Unit,
    onLike: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier.fillMaxWidth(),
        Arrangement.spacedBy(8.dp),
        Alignment.CenterVertically,
    ) {
        Button(
            onClick = onSkip,
            modifier = modifier.weight(1f, fill = true).preferredHeight(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.DislikeButton,
                contentColor = Color.White,
            )
        ) {
            Text(stringResource(id = R.string.edit_recipe_skip))
        }
        Button(
            onClick = onLike,
            modifier = modifier.weight(1f, fill = true).preferredHeight(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.LikeButton,
                contentColor = Color.White,
            )
        ) {
            Text(stringResource(id = R.string.edit_recipe_like))
        }
    }
}
