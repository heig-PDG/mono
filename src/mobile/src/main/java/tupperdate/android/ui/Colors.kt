package tupperdate.android.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.ScrollableRow
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

val Color.Companion.Smurf100 get() = Color(0xFFE6F3FF)
val Color.Companion.Smurf200 get() = Color(0xFFD3E9FE)
val Color.Companion.Smurf300 get() = Color(0xFFA8D5FF)
val Color.Companion.Smurf400 get() = Color(0xFF8EC8FF)
val Color.Companion.Smurf500 get() = Color(0xFF60B2FF)
val Color.Companion.Smurf600 get() = Color(0xFF339CFF)
val Color.Companion.Smurf700 get() = Color(0xFF0A88FF)
val Color.Companion.Smurf800 get() = Color(0xFF0069CC)
val Color.Companion.Smurf900 get() = Color(0xFF004280)

val Color.Companion.Flamingo100 get() = Color(0xFFFFE8FE)
val Color.Companion.Flamingo200 get() = Color(0xFFFECFFC)
val Color.Companion.Flamingo300 get() = Color(0xFFFCBEFA)
val Color.Companion.Flamingo400 get() = Color(0xFFFEAFFB)
val Color.Companion.Flamingo500 get() = Color(0xFFFF99FB)
val Color.Companion.Flamingo600 get() = Color(0xFFFF6BF9)
val Color.Companion.Flamingo700 get() = Color(0xFFCC00C4)
val Color.Companion.Flamingo800 get() = Color(0xFF990093)
val Color.Companion.Flamingo900 get() = Color(0xFF660062)

// Previews

@Composable
private fun ColorCell(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier.size(48.dp),
        color = color,
        shape = RoundedCornerShape(4.dp),
    ) {}
}

@Composable
private fun ColorList(
    colors: List<Color>,
    modifier: Modifier = Modifier,
    state: ScrollState = rememberScrollState(),
) {
    ScrollableRow(modifier, scrollState = state) {
        for ((index, color) in colors.withIndex()) {
            if (index == 0) {
                ColorCell(color, Modifier.padding(horizontal = 16.dp))
            } else {
                ColorCell(color, Modifier.padding(end = 16.dp))
            }
        }
    }
}

private val SmurfPalette = listOf(
    Color.Smurf100,
    Color.Smurf200,
    Color.Smurf300,
    Color.Smurf400,
    Color.Smurf500,
    Color.Smurf600,
    Color.Smurf700,
    Color.Smurf800,
    Color.Smurf900,
)

private val FlamingoPalette = listOf(
    Color.Flamingo100,
    Color.Flamingo200,
    Color.Flamingo300,
    Color.Flamingo400,
    Color.Flamingo500,
    Color.Flamingo600,
    Color.Flamingo700,
    Color.Flamingo800,
    Color.Flamingo900,
)

@Preview
@Composable
fun BrandingPreview() {

    MaterialTheme {
        Column {
            val state = rememberScrollState()

            Text("Palette", Modifier.padding(16.dp), style = MaterialTheme.typography.h5)
            ColorList(SmurfPalette, Modifier.padding(bottom = 16.dp), state = state)
            ColorList(FlamingoPalette, Modifier.padding(bottom = 16.dp), state = state)
        }
    }
}