package tupperdate.android.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val TupperdateLightColors = lightColors(
    onPrimary = Color.White,
    primary = Color.Smurf500,
    primaryVariant = Color.Smurf600,
    onSecondary = Color.White,
    secondary = Color.Flamingo500,
    secondaryVariant = Color.Flamingo600,
    surface = Color.White,
    onSurface = Color.Black,
)

private val TupperdateShapes = Shapes(
    large = RoundedCornerShape(32.dp),
    medium = RoundedCornerShape(16.dp),
    small = RoundedCornerShape(8.dp),
)

@Composable
fun TupperdateTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = TupperdateLightColors,
        shapes = TupperdateShapes,
        typography = TupperdateTypography,
        content = content,
    )
}
