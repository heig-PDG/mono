package tupperdate.android.ui.theme.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.graphics.TileMode

/**
 * A custom [Modifier] that draws a shade over the picture, using a cached draw layer. The
 * associated [LinearGradient] will only be re-created when the dimensions of the underlying
 * composable are invalidated.
 */
fun Modifier.shade(): Modifier = this then Modifier.drawWithCache {
    val gradient = Brush.linearGradient(
        listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
        start = Offset(size.width / 2, 0f),
        end = Offset(size.width / 2, size.height),
        tileMode = TileMode.Clamp,
    )
    onDrawWithContent {
        drawContent()
        drawRect(gradient)
    }
}
