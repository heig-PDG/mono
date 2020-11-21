package tupperdate.android.editRecipe

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonConstants
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LifecycleOwnerAmbient
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tupperdate.android.R
import tupperdate.android.ui.DislikeButton
import tupperdate.android.ui.LikeButton
import tupperdate.api.RecipeApi

@Composable
fun ViewRecipe(
    api: RecipeApi,
    recipe: RecipeApi.Recipe,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = LifecycleOwnerAmbient.current.lifecycleScope
    RecipeDetail(
        heroImage = recipe.pictureUrl,
        header = {
            Text(
                text = stringResource(id = R.string.edit_recipe_title),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.overline
            )
            Text(
                text = recipe.title,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.h5
            )
            ViewRecipeButtons(
                onSkip = {
                    scope.launch {
                        api.dislike(recipe)
                        onBack()
                    }
                },
                onLike = {
                    scope.launch {
                        api.like(recipe)
                        onBack()
                    }
                },
            )
        },
        icons = {
            // TODO : Actually read this data from the API.
            RecipeTags(
                vegan = false,
                hot = false,
                hasAllergens = false,
            )
        },
        // TODO : Actually read this from the API.
        description = {
            Text(
                text = stringResource(id = R.string.edit_recipe_description),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.overline
            )
            Text(
                text = recipe.description,
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
            colors = ButtonConstants.defaultButtonColors(
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
            colors = ButtonConstants.defaultButtonColors(
                backgroundColor = Color.LikeButton,
                contentColor = Color.White,
            )
        ) {
            Text(stringResource(id = R.string.edit_recipe_like))
        }
    }
}
