package tupperdate.android.ui.home.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.coil.CoilImage
import tupperdate.android.R
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.android.ui.theme.PlaceholderRecipeImage
import tupperdate.android.ui.theme.TupperdateTypography
import tupperdate.android.ui.theme.modifier.shade

@Composable
fun ProfileRecipe(
    recipe: Recipe,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .preferredHeight(ProfileRecipeHeight)
            .preferredWidth(ProfileRecipeWidth),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(Modifier.clickable(onClick = onClick)) {
            CoilImage(
                data = recipe.picture ?: PlaceholderRecipeImage,
                contentScale = ContentScale.Crop,
                fadeIn = true,
                loading = {
                    Image(
                        bitmap = imageResource(R.drawable.placeholder_recipe),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                },
                modifier = Modifier.fillMaxSize().shade(),
            )
            Text(
                text = recipe.title,
                style = TupperdateTypography.caption,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp).fillMaxWidth(),
            )
        }
    }
}

private val ProfileRecipeWidth = 120.dp
private val ProfileRecipeHeight = 160.dp
