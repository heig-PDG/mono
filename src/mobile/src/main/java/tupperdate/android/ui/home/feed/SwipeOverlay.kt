package tupperdate.android.ui.home.feed

import androidx.annotation.FloatRange
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * A composable that displays an overlay on top of a [RecipeCard]. This will typically be used to
 * indicate the swipe progress of the card.
 *
 * @param color the color of the overlay
 * @param progress how much progress there is in the animation
 * @param vector the [ImageVector] to render
 * @param modifier the modifier for this composable.
 */
@Composable
fun SwipeOverlay(
    color: Color,
    @FloatRange(from = 0.0, to = 1.0) progress: Float,
    vector: ImageVector,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier.background(color.copy(alpha = progress)),
        Alignment.Center,
    ) {
        val alpha = (progress * 4).coerceIn(0f, 1f)
        Image(vector, Modifier.scale(2f).alpha(alpha))
    }
}
