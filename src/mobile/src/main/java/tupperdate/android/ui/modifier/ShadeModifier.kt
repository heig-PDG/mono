package tupperdate.android.ui.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.graphics.TileMode

/**
 * A custom [Modifier] that draws a shade over the picture, using a cached draw layer. The
 * associated [LinearGradient] will only be re-created when the dimensions of the underlying
 * composable are invalidated.
 */
fun Modifier.shade(): Modifier = this then Modifier.drawWithCache {
    val gradient = LinearGradient(
        listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
        startX = size.width / 2,
        startY = 0f,
        endX = size.width / 2,
        endY = size.height,
        tileMode = TileMode.Clamp,
    )
    onDrawWithContent {
        drawContent()
        drawRect(gradient)
    }
}
