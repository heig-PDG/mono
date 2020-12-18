package tupperdate.android.ui.theme.material

import androidx.compose.animation.ColorPropKey
import androidx.compose.animation.animate
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.core.tween
import androidx.compose.animation.transition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tupperdate.android.ui.theme.Flamingo700
import tupperdate.android.ui.theme.Flamingo800
import tupperdate.android.ui.theme.Smurf700
import tupperdate.android.ui.theme.Smurf800
import java.util.*


private val FirstColor = ColorPropKey(label = "A")
private val SecondColor = ColorPropKey(label = "B")
private val ThirdColor = ColorPropKey(label = "C")
private val FourthColor = ColorPropKey(label = "D")

private enum class State {
    Start,
    End,
}

private val Transition = transitionDefinition<State> {
    state(State.Start) {
        this[FirstColor] = Color.Smurf700
        this[SecondColor] = Color.Flamingo800
        this[ThirdColor] = Color.Flamingo700
        this[FourthColor] = Color.Smurf800
    }
    state(State.End) {
        this[FirstColor] = Color.Flamingo700
        this[SecondColor] = Color.Smurf800
        this[ThirdColor] = Color.Smurf700
        this[FourthColor] = Color.Flamingo800
    }
    transition {
        FirstColor using infiniteRepeatable(
            animation = tween(3 * 1000),
            repeatMode = RepeatMode.Reverse,
        )
        SecondColor using infiniteRepeatable(
            animation = tween(4 * 1000),
            repeatMode = RepeatMode.Reverse,
        )
        ThirdColor using infiniteRepeatable(
            animation = tween(3 * 1000),
            repeatMode = RepeatMode.Reverse,
        )
        FourthColor using infiniteRepeatable(
            animation = tween(5 * 1000),
            repeatMode = RepeatMode.Reverse,
        )
    }
}

/**
 * An alternative to [Button] that uses an animated gradient on its inner border. The gradient
 * color scheme is Smurf- and Flamingo-based.
 *
 * @param onClick the callback called when the button is clicked.
 * @param modifier the modifier for the button.
 * @param borderSize the width of the gradient border.
 * @param shape the shape of the button.
 * @param content the inner contents of the button.
 */
@Composable
fun BrandedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderSize: Dp = 2.dp,
    shape: Shape = RoundedCornerShape(50),
    content: @Composable RowScope.() -> Unit,
) {
    // On first composition, we don't have access to our bounds. Use a best-effort guess.
    var bounds by remember { mutableStateOf(Rect(Offset(0f, 0f), Size(300f, 300f))) }
    val values = transition(
        definition = Transition, initState = State.Start,
        toState = State.End
    )
    val brush = Brush.linearGradient(
        0.0f to values[FirstColor],
        0.2f to values[SecondColor],
        0.5f to values[ThirdColor],
        0.85f to values[FourthColor],
        // Animated bounds changes.
        start = Offset(animate(bounds.left * 2), animate(bounds.top * 2)),
        end = Offset(animate(bounds.right * 2), animate(bounds.bottom * 2)),
        tileMode = TileMode.Mirror,
    )
    val stroke = BorderStroke(
        width = borderSize,
        brush = brush
    )
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black,
            backgroundColor = Color.White,
        ),
        border = stroke,
        shape = shape,
        modifier = modifier.onGloballyPositioned { bounds = it.boundsInParent },
        content = content,
    )
}

/**
 * An alternative to [Button] that uses an animated gradient on its inner border. The gradient
 * color scheme is Smurf- and Flamingo-based.
 *
 * @param value the string inside the button
 * @param onClick the callback called when the button is clicked.
 * @param modifier the modifier for the button.
 * @param shape the shape of the button.
 */
@Composable
fun BrandedButton(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(50),
) {
    BrandedButton(
        onClick = onClick,
        modifier = modifier
            .preferredHeight(56.dp),
        shape = shape,
    ) {
        Text(
            text = (value).toUpperCase(Locale.getDefault()),
            fontSize = 14.sp
        )
    }
}
