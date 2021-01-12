package tupperdate.android.ui.home.chats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tupperdate.android.R
import tupperdate.android.data.features.messages.Message
import tupperdate.android.data.features.messages.Sender
import tupperdate.android.ui.theme.TupperdateTheme
import tupperdate.android.ui.theme.components.ProfilePicture

// TODO : Rename to Chat.
@Composable
fun OneConversation(
    displayName: String,
    displayPicture: Any,
    messages: List<Message>,
    onBack: () -> Unit,
    onSendMessageClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier,
        topBar = {
            ChatTopBar(
                title = displayName,
                picture = displayPicture,
                onBack = onBack,
            )
        },
        bodyContent = {
            LazyColumn(
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(messages) {
                    ChatMessage(it)
                }
            }
        },
        bottomBar = {
            val (message, setMessage) = remember { mutableStateOf("") }
            MessageDraft(
                value = message,
                onValueChange = setMessage,
                onSubmit = {
                    onSendMessageClick(message)
                    setMessage("")
                }
            )
        }
    )
}

// TODO : Tweak TopBar composable.
@Composable
private fun ChatTopBar(
    title: String,
    picture: Any,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onBack) {
                Icon(vectorResource(R.drawable.ic_back_arrow))
            }
        },
        title = {
            Row(
                Modifier,
                Arrangement.spacedBy(8.dp),
                Alignment.CenterVertically
            ) {
                ProfilePicture(
                    image = picture,
                    highlighted = false,
                    modifier = Modifier.preferredSize(24.dp),
                )
                Text(title)
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun MessageDraft(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
        onImeActionPerformed = { _, _ -> onSubmit() },
    )
}

@Preview
@Composable
private fun OneConversationPreview() {
    val messages = remember {
        listOf(
            Message(
                identifier = "",
                timestamp = System.currentTimeMillis(),
                body = "Do you dream of Scorchers ?",
                from = Sender.Myself,
            ),
            Message(
                identifier = "",
                timestamp = System.currentTimeMillis(),
                body = "Only when I miss hunting one",
                from = Sender.Other,
            ),
        )
    }
    TupperdateTheme {
        OneConversation(
            displayName = "Aloy",
            displayPicture = "https://pbs.twimg.com/profile_images/1257192502916001794/f1RW6Ogf_400x400.jpg",
            messages = messages,
            onBack = { },
            onSendMessageClick = { },
        )
    }
}
