package tupperdate.android.ui.home.chats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.preferredSize
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
import tupperdate.android.ui.theme.components.ProfilePicture

@Composable
fun Conversation(
    title: String,
    subtitle: String,
    highlighted: Boolean,
    image: Any,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier,
        Arrangement.spacedBy(16.dp),
        Alignment.CenterVertically,
    ) {
        ProfilePicture(
            image = image,
            highlighted = highlighted,
            modifier = Modifier.preferredSize(56.dp)
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
