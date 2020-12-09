package tupperdate.android.ui.theme.components

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.coil.CoilImage
import tupperdate.android.ui.theme.modifier.dotted

/**
 * A [Composable] which is used to display a user profile picture in a branded way.
 * Our branding consists of having the image in a circle with a small grey border around it.
 *
 * If the [ProfilePicture] is highlighted, a small blue dot will be shown.
 *
 * @param image Is a value that can be displayed by a [CoilImage]
 * @param highlighted true if a small blue dot should be shown
 */
@Composable
fun ProfilePicture(
    image: Any,
    highlighted: Boolean,
    modifier: Modifier = Modifier,
) {
    CoilImage(
        data = image,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .dotted(highlighted)
            .clip(CircleShape)
            .border(4.dp, Color.Black.copy(alpha = 0.2f), CircleShape)
    )
}
