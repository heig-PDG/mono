package tupperdate.android.home

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BarButton(
    size: Int,
    onClick: () -> Unit,
    backgroundColor: Color,
    content: @Composable () -> Unit
) {
    Button(
        modifier = Modifier.size(size.dp),
        onClick = onClick,
        shape = CircleShape,
        backgroundColor = backgroundColor,
        contentColor = Color.White
    ) {
        content()
    }
}

const val littleButtonSize = 45
const val normalButtonSize = 50
const val veryLittlButtonSize = 30
