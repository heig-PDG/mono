package tupperdate.android.ui.home.chats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.AmbientContentAlpha
import androidx.compose.material.ContentAlpha.high
import androidx.compose.material.ContentAlpha.medium
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * A [Conversation] is a row that lets the user open a chat with a specific user. It displays some
 * recent information about the conversation, and more specifically, the latest messages that have
 * been sent or received by the user.
 *
 * @param title the first line of text.
 * @param subtitle the second line of text.
 * @param highlighted true if the row should be displayed with higher emphasis.
 * @param image the icon to display.
 * @param modifier the modifier for this composable.g
 */
@Composable
fun Conversation(
    title: String,
    subtitle: String,
    highlighted: Boolean,
    image: Any?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier,
        Arrangement.spacedBy(16.dp),
        Alignment.CenterVertically,
    ) {
        Bubble(
            picture = image,
            onClick = { /* Ignored.*/ },
            enabled = false,
        )
        Column {
            val emphasis = if (highlighted) high else medium
            Providers(AmbientContentAlpha provides emphasis) {
                Text(
                    text = title,
                    maxLines = 1,
                    style = typography.subtitle1,
                    overflow = TextOverflow.Ellipsis,
                )
                Providers(AmbientContentAlpha provides medium) {
                    Text(
                        text = subtitle,
                        maxLines = 1,
                        style = typography.subtitle2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}
