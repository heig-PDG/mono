package tupperdate.android.ui.material

import androidx.compose.animation.ColorPropKey
import androidx.compose.animation.animate
import androidx.compose.animation.core.AnimationConstants.Infinite
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.core.tween
import androidx.compose.animation.transition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.onPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tupperdate.android.ui.Flamingo700
import tupperdate.android.ui.Flamingo800
import tupperdate.android.ui.Smurf700
import tupperdate.android.ui.Smurf800


private val FirstColor = ColorPropKey()
private val SecondColor = ColorPropKey()
private val ThirdColor = ColorPropKey()
private val FourthColor = ColorPropKey()

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
        FirstColor using repeatable(
            animation = tween(3 * 1000),
            iterations = Infinite,
            repeatMode = RepeatMode.Reverse,
        )
        SecondColor using repeatable(
            animation = tween(4 * 1000),
            iterations = Infinite,
            repeatMode = RepeatMode.Reverse,
        )
        ThirdColor using repeatable(
            animation = tween(3 * 1000),
            iterations = Infinite,
            repeatMode = RepeatMode.Reverse,
        )
        FourthColor using repeatable(
            animation = tween(5 * 1000),
            iterations = Infinite,
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
 * @param content the inner contents of the button.
 */
@Composable
fun GradientButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderSize: Dp = 2.dp,
    content: @Composable RowScope.() -> Unit,
) {
    // On first composition, we don't have access to our bounds. Use a best-effort guess.
    var bounds by remember { mutableStateOf(Rect(Offset(0f, 0f), Size(300f, 300f))) }
    val values = transition(
        definition = Transition, initState = State.Start,
        toState = State.End
    )
    val brush = LinearGradient(
        0.0f to values[FirstColor],
        0.2f to values[SecondColor],
        0.5f to values[ThirdColor],
        0.85f to values[FourthColor],
        // Animated bounds changes.
        startX = animate(bounds.left * 2),
        startY = animate(bounds.top * 2),
        endX = animate(bounds.right * 2),
        endY = animate(bounds.bottom * 2),
        tileMode = TileMode.Mirror,
    )
    val stroke = BorderStroke(
        width = borderSize,
        brush = brush
    )
    Button(
        onClick = onClick,
        contentColor = Color.Black,
        backgroundColor = Color.White,
        border = stroke,
        shape = RoundedCornerShape(50),
        modifier = modifier.onPositioned { bounds = it.boundsInParent },
        content = content,
    )
}