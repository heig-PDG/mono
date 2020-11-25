package tupperdate.android.chats

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import tupperdate.android.appbars.ChatTopBar
import tupperdate.android.appbars.bottomBar
import tupperdate.android.ui.GreyBackgroundMessage
import tupperdate.android.ui.Smurf200
import tupperdate.android.ui.TupperdateTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Message(
    val textMessage: String,
    val iSend: Boolean
)

@Composable
fun OneConversation(
    msgList:List<Message>,
    onReturnClick: () -> Unit,
    imageOther: Any,
    nameOther: String,
    otherOnline: String,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier,
        topBar = {
            ChatTopBar(
                onReturnClick = onReturnClick,
                imageOther = imageOther,
                nameOther = nameOther,
                otherOnline = otherOnline
            )
        },
        bodyContent = { MessagesColumn(msgList = msgList) },
        bottomBar = { bottomBar(onSendClick = onSendClick) }
    )
}

@Composable
private fun MessagesColumn(
    msgList:List<Message>,
    modifier: Modifier = Modifier
) {
    LazyColumnFor(items = msgList) {
        DisplayMessage(
            message = it
        )
    }
}

@Composable
private fun DisplayMessage(
    message: Message,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.background(Color.Transparent)
            .fillMaxWidth(1f)
            .padding(top = 16.dp),
        horizontalArrangement = (if (message.iSend) Arrangement.Start else Arrangement.End)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(0.75f)
                .background(if (message.iSend) Color.Smurf200 else Color.GreyBackgroundMessage)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = message.textMessage,
                    style = MaterialTheme.typography.caption,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = LocalDateTime.now().format(
                            DateTimeFormatter.ofPattern("HH:mm")
                        ),
                        style = MaterialTheme.typography.caption
                    )
                    /*TODO add the blue "checked" icon*/
                }
            }
        }
    }
}

@Preview
@Composable
private fun OneConversationPreview() {
    val msgList = listOf<Message>(
        Message("Do you dream of Scorchers ?", true),
        Message("Only when I missed hunting one", true)
    )
    TupperdateTheme {
        OneConversation(
            msgList = msgList,
            onReturnClick = {},
            imageOther = "https://pbs.twimg.com/profile_images/1257192502916001794/f1RW6Ogf_400x400.jpg",
            nameOther = "Aloy",
            otherOnline = "online",
            onSendClick = {}
        )
    }
}
