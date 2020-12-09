package tupperdate.android.ui.theme.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Makes an item overlap by a certain amount outside of its bounds.
 *
 * @param start the start overlap amount.
 * @param end the end overlap amount.
 * @param top the top overlap amount.
 * @param bottom the bottom overlap amount.
 */
fun Modifier.overlap(
    start: Dp = 0.dp,
    end: Dp = 0.dp,
    top: Dp = 0.dp,
    bottom: Dp = 0.dp
) = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    layout(
        placeable.width - start.toIntPx() - end.toIntPx(),
        placeable.height - top.toIntPx() - bottom.toIntPx(),
    ) {
        placeable.placeRelative(
            x = -start.toIntPx(),
            y = -top.toIntPx(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OverlapModifierPreview() {
    Column(Modifier.background(Color.Blue)) {
        Box(
            Modifier
                .background(Color.Red)
                .size(72.dp),
        )
        Box(
            Modifier
                .overlap(top = 8.dp)
                .clip(RoundedCornerShape(8.dp))
                .width(72.dp)
                .heightIn(min = 72.dp)
                .background(Color.Green),
        )
    }
}
