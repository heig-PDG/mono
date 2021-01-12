package tupperdate.android.ui.home.recipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.coil.CoilImage
import tupperdate.android.R
import tupperdate.android.ui.theme.modifier.overlap

/**
 * A composable that displays the recipe details, with different slots available
 *
 * @param heroImage the image to display as the hero content
 * @param header the header composable
 * @param icons the icons composable
 * @param description the description composable
 * @param onClose a callback called when the close icon is pressed.
 * @param modifier the [Modifier] for this composable
 * @param onEdit a callback called when the edit icon is pressed. If null, no icon is shown.
 *
 * @see EditRecipe
 */
@Composable
fun RecipeDetail(
    heroImage: Any,
    header: @Composable ColumnScope.() -> Unit,
    icons: @Composable () -> Unit,
    description: @Composable ColumnScope.() -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    onEdit: (() -> Unit)? = null,
) {

    Column(modifier) {
        Box {
            CoilImage(
                data = heroImage,
                fadeIn = true,
                loading = { Image(imageResource(R.drawable.placeholder_recipe)) },
                modifier = Modifier
                    .preferredHeight(290.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop,
            )
            IconButton(onClose, Modifier.align(Alignment.TopEnd).padding(16.dp)) {
                Icon(vectorResource(R.drawable.ic_home_dislike_recipe))
            }
            if (onEdit != null) {
                IconButton(
                    onEdit, Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .padding(bottom = 8.dp) // Anti-overlap.
                ) {
                    Icon(vectorResource(R.drawable.ic_editrecipe_edit), tint = Color.White)
                }
            }
        }
        // TODO : Introduce a custom scrollable layout.
        Surface(
            Modifier.overlap(top = 8.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = 2.dp,
        ) {
            ScrollableColumn(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                header()
                icons()
                Divider()
                description()
            }
        }
    }
}
