package tupperdate.android.ui.home.chats

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import dev.chrisbanes.accompanist.coil.CoilImage

/**
 * A stack of [Bubble] representing the different recipes that have been matched for a single user.
 *
 * @param pictures the [String] urls to display for recipes.
 * @param onClick the callback to call when the conversation is clicked.
 * @param modifier the composable modifier.
 */
@Composable
fun MatchBubble(
    pictures: List<String?>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayed = remember(pictures) {
        pictures.asSequence()
            .take(MatchBubbleStackHeight)
            .sortedBy { it }
            .toList()
    }
    Box(modifier) {
        displayed.fastForEachIndexed { index, url ->
            Bubble(
                picture = url,
                onClick = onClick,
                modifier = Modifier.padding(start = 8.dp * index),
                enabled = true,
            )
        }
    }
}

private const val MatchBubbleStackHeight = 3

/**
 * A composable that displays an individual chat bubble.
 *
 * @param picture the individual picture to display in a bubble.
 * @param onClick the callback to call when the bubble is clicked.
 * @param modifier the composable modifier.
 * @param enabled true if the click listener is enabled.
 *
 * @see MatchBubble a stacked version of a [Bubble]
 */
@Composable
fun Bubble(
    picture: Any?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
) {
    CoilImage(
        data = picture ?: "", // TODO : Handle missing images.
        contentScale = ContentScale.Crop,
        modifier = modifier
            .preferredSize(56.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick, enabled = enabled)
            .border(8.dp, Color.Black.copy(alpha = 0.2f), CircleShape)
    )
}
