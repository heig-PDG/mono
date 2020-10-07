package tupperdate.android.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TupperdateLightColors = lightColors(
    onPrimary = Color.White,
    primary = Color.Smurf500,
    primaryVariant = Color.Smurf600,
    onSecondary = Color.White,
    secondary = Color.Flamingo500,
    secondaryVariant = Color.Flamingo600,
)

private val TupperdateShapes = Shapes()

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