package tupperdate.android.ui.home.chats

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tupperdate.android.data.features.messages.Message
import tupperdate.android.data.features.messages.Sender
import tupperdate.android.ui.theme.Smurf100
import tupperdate.android.ui.theme.Smurf200
import tupperdate.android.ui.theme.TupperdateTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * A [ChatMessage] displays a single conversation element.
 *
 * @param message the [Message] to display
 * @param modifier the modifier for this composable
 */
@Composable
fun ChatMessage(
    message: Message,
    modifier: Modifier = Modifier,
) {
    val color =
        if (message.from == Sender.Myself) Color.Smurf200
        else Color.Smurf100
    ChatMessage(
        body = message.body,
        timestamp = message.timestamp,
        color = color,
        modifier = modifier,
    )
}

/**
 * A [ChatMessage] displays a single conversation element. To clearly indicate the sender, its
 * background color should be altered.
 *
 * @param body the body of the message
 * @param timestamp when the message was sent, in UNIX epoch millis
 * @param modifier the modifier for this composable
 * @param color the background color of the chat message
 */
@Composable
fun ChatMessage(
    body: String,
    timestamp: Long,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.surface,
) {
    // Format time based on the current locale.
    val formatted = remember(timestamp) {
        Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("hh:mm"))
    }

    // Render the chat message itself.
    Surface(
        modifier = modifier,
        color = color,
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp,
    ) {
        Column(
            Modifier.padding(8.dp),
            Arrangement.spacedBy(8.dp),
        ) {
            Providers(AmbientContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = body,
                    style = MaterialTheme.typography.body1,
                )
                Text(
                    text = formatted,
                    modifier = Modifier.align(Alignment.End),
                    style = MaterialTheme.typography.caption,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ChatMessagePreview() = TupperdateTheme {
    Scaffold {
        val timestamp = remember { System.currentTimeMillis() }
        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                ChatMessage(
                    body = "Hello world.",
                    timestamp = timestamp,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(.9f),
                )
                ChatMessage(
                    body = "Hello world. This is a much larger text that spans multiple lines.",
                    timestamp = timestamp,
                    color = Color.Smurf200,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(.9f),
                )
            }
        }
    }
}
