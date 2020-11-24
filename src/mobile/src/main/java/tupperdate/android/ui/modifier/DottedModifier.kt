package tupperdate.android.ui.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import tupperdate.android.ui.Smurf500
import kotlin.math.sqrt

/**
 * A [Modifier] which can draw a dot if the underlying layer currently requires attention. It will
 * be drawn on a 45-degree line of the biggest inner circle that could fit the composable.
 *
 * @param visible true if the circle should be drawn, false otherwise.
 */
fun Modifier.dotted(visible: Boolean) = this then Modifier.drawWithCache {
    val radius = size.minDimension / 2 - 2.dp.toPx()
    val offset = Offset(
        x = radius * sqrt(2f) / 2f,
        y = radius * sqrt(2f) / -2f,
    )
    onDrawWithContent {
        drawContent()
        if (visible) {
            drawCircle(Color.White, 6.dp.toPx(), size.center() + offset)
            drawCircle(Color.Smurf500, 4.dp.toPx(), size.center() + offset)
        }
    }
}
