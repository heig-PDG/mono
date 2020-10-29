package tupperdate.android.ui.material

import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Typeface
import androidx.compose.animation.ColorPropKey
import androidx.compose.animation.animate
import androidx.compose.animation.core.*
import androidx.compose.animation.transition
import androidx.compose.foundation.AmbientTextStyle
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.onGloballyPositioned
import androidx.compose.ui.platform.DensityAmbient
import androidx.ui.tooling.preview.Preview
import tupperdate.android.ui.Flamingo700
import tupperdate.android.ui.Flamingo800
import tupperdate.android.ui.Smurf700
import tupperdate.android.ui.Smurf800

private val FirstColor = ColorPropKey()
private val SecondColor = ColorPropKey()
private val ThirdColor = ColorPropKey()
private val FourthColor = ColorPropKey()

private enum class BrandedTitleTextState {
    Start,
    End,
}

private val Transition = transitionDefinition<BrandedTitleTextState> {
    state(BrandedTitleTextState.Start) {
        this[FirstColor] = Color.Smurf700
        this[SecondColor] = Color.Flamingo800
        this[ThirdColor] = Color.Flamingo700
        this[FourthColor] = Color.Smurf800
    }
    state(BrandedTitleTextState.End) {
        this[FirstColor] = Color.Flamingo700
        this[SecondColor] = Color.Smurf800
        this[ThirdColor] = Color.Smurf700
        this[FourthColor] = Color.Flamingo800
    }
    transition {
        FirstColor using repeatable(
            animation = tween(3 * 1000),
            iterations = AnimationConstants.Infinite,
            repeatMode = RepeatMode.Reverse,
        )
        SecondColor using repeatable(
            animation = tween(4 * 1000),
            iterations = AnimationConstants.Infinite,
            repeatMode = RepeatMode.Reverse,
        )
        ThirdColor using repeatable(
            animation = tween(3 * 1000),
            iterations = AnimationConstants.Infinite,
            repeatMode = RepeatMode.Reverse,
        )
        FourthColor using repeatable(
            animation = tween(5 * 1000),
            iterations = AnimationConstants.Infinite,
            repeatMode = RepeatMode.Reverse,
        )
    }
}

/**
 * An equivalent to the text composable that does not support line wrapping but displays an
 * animated gradient. Useful for hero content.
 *
 * @param value the contents to display in the [BrandedTitleText]
 * @param modifier a [Modifier] for this composable.
 */
@Composable
fun BrandedTitleText(
    value: String,
    modifier: Modifier = Modifier,
) {
    // On first composition, we don't have access to our bounds. Use a best-effort guess.
    var bounds by remember { mutableStateOf(Rect(Offset(0f, 0f), Size(300f, 300f))) }

    // Calculate the animated transition colors.
    val values = transition(
        definition = Transition, initState = BrandedTitleTextState.Start,
        toState = BrandedTitleTextState.End
    )
    val shader = LinearGradientShader(
        from = Offset(animate(bounds.left), animate(bounds.top)),
        to = Offset(animate(bounds.right), animate(bounds.bottom)),
        colors = listOf(
            values[FirstColor],
            values[SecondColor],
            values[ThirdColor],
            values[FourthColor]
        ),
        colorStops = listOf(0.0f, 0.2f, 0.5f, 0.85f),
        tileMode = TileMode.Mirror
    )

    // Text styling.
    val textStyle = AmbientTextStyle.current
    val sizeFloat = with(DensityAmbient.current) { textStyle.fontSize.toPx() }
    val sizeDp = with(DensityAmbient.current) { textStyle.fontSize.toDp() }

    // Paint preparation.
    val typeface = remember { Typeface.create(Typeface.DEFAULT, Typeface.BOLD) }
    val paint = remember(shader) {
        Paint().asFrameworkPaint().apply {
            this.isAntiAlias = true
            this.textSize = sizeFloat
            this.typeface = typeface
            this.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
            this.shader = shader
        }
    }

    Canvas(modifier
        .height(sizeDp)
        .onGloballyPositioned { bounds = it.boundsInParent }) {
        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawText(value, 0f, sizeFloat, paint)
        }
    }
}

@Composable
@Preview
private fun BrandedTitleTextPreview() {
    BrandedTitleText(value = "Hello world")
}