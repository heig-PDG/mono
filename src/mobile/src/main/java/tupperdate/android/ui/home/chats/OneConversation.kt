package tupperdate.android.ui.home.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tupperdate.android.ui.theme.GreyBackgroundMessage
import tupperdate.android.ui.theme.Smurf200
import tupperdate.android.ui.theme.TupperdateTheme
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
    otherOnlineStatus: String,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier,
        topBar = {
            ChatTopBar(
                onReturnClick = onReturnClick,
                imageOther = imageOther,
                nameOther = nameOther,
                otherOnline = otherOnlineStatus
            )
        },
        bodyContent = { MessagesColumn(msgList = msgList) },
        bottomBar = { ChatBottomBar(onSendClick = onSendClick) }
    )
}

@Composable
private fun MessagesColumn(
    msgList:List<Message>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {
        items(msgList){
            DisplayMessage(it)
        }
    }
}

@Composable
private fun DisplayMessage(
    message: Message,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.background(Color.Transparent)
            .fillMaxWidth(1f)
            .padding(top = 16.dp),
        horizontalArrangement = (if (message.iSend) Arrangement.End else Arrangement.Start)
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
    val msgList = listOf(
        Message("Do you dream of Scorchers ?", true),
        Message("Only when I miss hunting one", false)
    )
    TupperdateTheme {
        OneConversation(
            msgList = msgList,
            onReturnClick = {},
            imageOther = "https://pbs.twimg.com/profile_images/1257192502916001794/f1RW6Ogf_400x400.jpg",
            nameOther = "Aloy",
            otherOnlineStatus = "online",
            onSendClick = {}
        )
    }
}
