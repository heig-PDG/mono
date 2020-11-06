package tupperdate.android.home

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonConstants
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BarButton(
    size: Int,
    onClick: () -> Unit,
    backgroundColor: Color,
    content: @Composable() () -> Unit,
    modifier: Modifier = Modifier,
) {
    BarButton(
        modifier = modifier, size = size,
        onClick = onClick, backgroundColor = backgroundColor,
        contentColor = Color.White, content = content
    )
}

@Composable
fun BarButton(
    size: Int,
    onClick: () -> Unit,
    backgroundColor: Color,
    contentColor: Color,
    content: @Composable() () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier.size(size.dp),
        shape = CircleShape,
        onClick = onClick,
        colors = ButtonConstants.defaultButtonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        ),
        elevation = null
    ) {
        content()
    }
}

const val littleButtonSize = 45
const val normalButtonSize = 50
const val veryLittleButtonSize = 30
