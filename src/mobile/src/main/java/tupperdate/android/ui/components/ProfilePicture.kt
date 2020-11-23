package tupperdate.android.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.coil.CoilImage
import tupperdate.android.ui.modifier.dotted

@Composable
fun ProfilePicture(
    image: Any,
    highlighted: Boolean,
    modifier: Modifier = Modifier,
) {
    CoilImage(
        image, modifier
            .dotted(highlighted)
            .clip(CircleShape)
            .size(56.dp)
            .border(4.dp, Color.Black.copy(alpha = 0.2f), CircleShape)
    )
}
