package tupperdate.android.home.chats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.AmbientContentAlpha
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import tupperdate.android.ui.TupperdateTheme
import tupperdate.android.ui.components.ProfilePicture

data class Conversation(
    val id: String,
    val title: String,
    val subtitle: String,
    val highlighted: Boolean,
    val image: Any,
)

@Composable
fun Chats(
    conversations: List<Conversation>,
    onConversationClick: (Conversation) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumnFor(conversations, modifier) {
        Conversation(
            title = it.title,
            subtitle = it.subtitle,
            highlighted = it.highlighted,
            image = it.image,
            onClick = { onConversationClick(it) }
        )
    }
}

@Composable
private fun Conversation(
    title: String,
    subtitle: String,
    highlighted: Boolean,
    image: Any,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier.clickable(onClick = onClick).padding(vertical = 8.dp),
        Arrangement.spacedBy(16.dp),
        Alignment.CenterVertically
    ) {
        ProfilePicture(
            image,
            highlighted,
            Modifier.size(56.dp)
        )

        // TODO update this properly
        val emphasis = if (highlighted) ContentAlpha.high
        else ContentAlpha.medium
        Providers(AmbientContentAlpha provides emphasis) {
            Column {
                Text(title, maxLines = 1, style = typography.subtitle1, overflow = TextOverflow.Ellipsis)
                Providers(AmbientContentAlpha provides ContentAlpha.medium) {
                    Text(subtitle, maxLines = 1, style = typography.subtitle2, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConversationPreviewHighlighted() {
    TupperdateTheme {
        Conversation(
            title = "Mario",
            subtitle = "Hey Luigi you forgot your pipe in my garden during our last party",
            highlighted = true,
            image = "https://thispersondoesnotexist.com/image",
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ConversationPreviewNormal() {
    TupperdateTheme {
        Conversation(
            title = "Luigi",
            subtitle = "Damn that's right sorry bro I'll pick it up today today",
            highlighted = false,
            image = "https://thispersondoesnotexist.com/image",
            onClick = {},
        )
    }
}
