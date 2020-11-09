package tupperdate.android.home

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonConstants
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BarButton(
    modifier: Modifier = Modifier,
    size: Dp,
    onClick: () -> Unit,
    backgroundColor: Color,
    contentColor: Color= Color.White,
    content: @Composable () -> Unit,
) {
    Button(
        modifier = modifier.size(size),
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

val LittleButtonSize = 45.dp
val NormalButtonSize = 50.dp
val VeryLittleButtonSize = 30.dp

