package tupperdate.android.ui.material

import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Shader
import android.graphics.Typeface
import androidx.compose.foundation.AmbientTextStyle
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.DensityAmbient
import androidx.ui.tooling.preview.Preview

@Composable
fun BrandedTitleText(
    value: String,
    modifier: Modifier = Modifier,
) {
    val textStyle = AmbientTextStyle.current
    val paint = Paint().asFrameworkPaint()
    val gradientShader: Shader = LinearGradientShader(
        from = Offset(0f, 0f),
        to = Offset(10f, 10f),
        listOf(Blue, Cyan),
        tileMode = TileMode.Repeated
    )
    val sizeFloat = with(DensityAmbient.current) { textStyle.fontSize.toPx() }
    val sizeDp = with(DensityAmbient.current) { textStyle.fontSize.toDp() }

    Canvas(modifier.height(sizeDp)) {
        paint.apply {
            isAntiAlias = true
            textSize = sizeFloat
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
            shader = gradientShader
        }
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