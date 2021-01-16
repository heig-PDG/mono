package tupperdate.android.ui.home.recipe

import androidx.compose.animation.animate
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.coil.CoilImage
import tupperdate.android.R
import tupperdate.android.ui.theme.InactiveIcons
import tupperdate.android.ui.theme.Smurf800
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
    Box(modifier) {

        val state = rememberScrollState()

        ScrollableColumn(scrollState = state) {
            Box {
                CoilImage(
                    data = heroImage,
                    fadeIn = true,
                    loading = {
                        Image(
                            bitmap = imageResource(R.drawable.placeholder_recipe),
                            contentScale = ContentScale.Crop,
                        )
                    },
                    modifier = Modifier
                        .preferredHeight(290.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                )
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
            Surface(
                modifier = Modifier.overlap(top = 8.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = 2.dp,
            ) {
                Column(
                    Modifier.padding(16.dp),
                    Arrangement.spacedBy(16.dp),
                    Alignment.CenterHorizontally,
                ) {
                    header()
                    icons()
                    Divider()
                    description()
                }
            }
        }

        // Close button, displayed on top of the recipe composable.
        CloseButton(
            raised = state.value != 0f,
            onClick = onClose,
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp),
        )
    }
}

@Composable
private fun CloseButton(
    raised: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val elevation = animate(if (raised) 8.dp else 0.dp)
    val alpha = animate(if (raised) 1f else 0.5f)
    val content = animate(if (raised) Color.InactiveIcons else Color.Smurf800)

    Surface(
        elevation = elevation,
        shape = CircleShape,
        contentColor = content,
        modifier = modifier.alpha(alpha).size(36.dp),
    ) {
        IconButton(onClick, Modifier) {
            Icon(vectorResource(R.drawable.ic_editrecipe_close))
        }
    }
}
