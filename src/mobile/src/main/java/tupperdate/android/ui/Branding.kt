package tupperdate.android.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import tupperdate.android.ui.material.BrandedButton
import java.util.*

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
    TupperdateTheme {
        ScrollableColumn {
            val state = rememberScrollState()

            val padding = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp)

            Text("Palette", Modifier.padding(16.dp), style = MaterialTheme.typography.h5)
            ColorList(SmurfPalette, Modifier.padding(bottom = 16.dp), state = state)
            ColorList(FlamingoPalette, Modifier.padding(bottom = 16.dp), state = state)

            Divider()

            Text("H1 Headline", padding, style = MaterialTheme.typography.h1)
            Text("H2 Headline", padding, style = MaterialTheme.typography.h2)
            Text("H3 Headline", padding, style = MaterialTheme.typography.h3)
            Text("H4 Headline", padding, style = MaterialTheme.typography.h4)
            Text("H5 Headline", padding, style = MaterialTheme.typography.h5)
            Text("H6 Headline", padding, style = MaterialTheme.typography.h6)

            Text("Subtitle 1", padding, style = MaterialTheme.typography.subtitle1)
            Text("Subtitle 2", padding, style = MaterialTheme.typography.subtitle2)

            Text("Body 1", padding, style = MaterialTheme.typography.body1)
            Text("Body 2", padding, style = MaterialTheme.typography.body2)

            Text("Caption", padding, style = MaterialTheme.typography.caption)
            Text(
                "Overline".toUpperCase(Locale.getDefault()),
                padding,
                style = MaterialTheme.typography.overline
            )

            Button(onClick = {}, padding) {
                Text("Button")
            }

            BrandedButton(onClick = {}, modifier = padding) {
                Text("Gradient Button")
            }
        }
    }
}