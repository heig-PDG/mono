package tupperdate.android.ui.home.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.emptyContent
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.coil.CoilImage
import tupperdate.android.R
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.android.ui.theme.Flamingo500
import tupperdate.android.ui.theme.PlaceholderRecipeImage
import tupperdate.android.ui.theme.TupperdateTheme
import tupperdate.android.ui.theme.modifier.shade


/**
 * A composable that displays a certain recipe.
 *
 * @param recipe the [Recipe] data to display.
 * @param onInfoClick callback called whenever the info button is clicked.
 * @param modifier the [Modifier] for this composable.
 * @param warning an eventual warning string to be displayed on the card.
 * @param overlay an overlay for this [RecipeCard]
 */
@Composable
fun RecipeCard(
    recipe: Recipe,
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier,
    warning: String? = null,
    overlay: @Composable () -> Unit = emptyContent(),
) {
    RecipeCard(
        title = recipe.title,
        subtitle = recipe.description,
        imageUrl = recipe.picture,
        warning = warning,
        onInfoClick = onInfoClick,
        modifier = modifier,
        overlay = overlay,
    )
}

/**
 * A composable that displays a certain recipe, along with some information.
 *
 * @param title the title of the displayed recipe
 * @param subtitle the subtitle of the displayed recipe. Should be formatted
 * @param imageUrl the URL at which the recipe image should be fetched
 * @param warning an eventual warning string to be displayed on the card.
 * @param onInfoClick callback called whenever the info button is clicked
 * @param modifier the [Modifier] for this composable
 * @param overlay an overlay for this [RecipeCard]
 */
@Composable
fun RecipeCard(
    title: String,
    subtitle: String,
    imageUrl: String?,
    warning: String?,
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier,
    overlay: @Composable () -> Unit = emptyContent(),
) {
    Card(
        modifier.preferredSize(width = 308.dp, height = 362.dp),
        elevation = 4.dp,
        contentColor = Color.White,
    ) {
        Box {
            // Background that fills the card.
            CoilImage(
                data = imageUrl ?: PlaceholderRecipeImage,
                modifier = Modifier.shade().fillMaxSize(),
                fadeIn = true,
                loading = {
                    Image(
                        bitmap = imageResource(R.drawable.placeholder_recipe),
                        contentScale = ContentScale.Crop,
                    )
                },
                contentScale = ContentScale.Crop,
            )

            // Recipe information.
            Column(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp, vertical = 32.dp),
            ) {
                if (warning != null) {
                    Warning(warning)
                    Spacer(Modifier.height(8.dp))
                }
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(title, style = MaterialTheme.typography.h6)
                        Text(subtitle, style = MaterialTheme.typography.subtitle2)
                    }
                    Spacer(Modifier.weight(1f, true))
                    IconButton(onInfoClick) {
                        Icon(vectorResource(R.drawable.ic_home_recipe_card_help))
                    }
                }
            }

            overlay()
        }
    }
}

@Composable
private fun Warning(
    message: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier,
        Arrangement.spacedBy(8.dp),
        Alignment.CenterVertically,
    ) {
        Providers(AmbientContentColor provides Color.Flamingo500) {
            Icon(vectorResource(R.drawable.ic_recipe_warning))
            Text(message, style = MaterialTheme.typography.subtitle2)
        }
    }
}

// PREVIEWS

@Preview
@Composable
private fun RecipeCardPreview() {
    TupperdateTheme {
        RecipeCard(
            title = "Chilli con carne",
            subtitle = "575 Kcal.",
            imageUrl = "https://thispersondoesnotexist.com/image",
            warning = "This recipe is not vegetarian.",
            onInfoClick = {},
            Modifier.padding(16.dp),
        )
    }
}
