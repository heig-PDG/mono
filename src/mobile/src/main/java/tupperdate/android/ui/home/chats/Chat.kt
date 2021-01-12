package tupperdate.android.ui.home.chats

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tupperdate.android.R
import tupperdate.android.data.features.messages.Message
import tupperdate.android.data.features.messages.Sender
import tupperdate.android.ui.theme.TupperdateTheme
import tupperdate.android.ui.theme.components.ProfilePicture
import tupperdate.android.ui.theme.plus

// TODO : Rename to Chat.
@Composable
fun Chat(
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
        bodyContent = { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp) + padding,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                reverseLayout = true,
            ) {
                items(messages) {
                    Box(Modifier.fillMaxWidth()) {
                        val position = Modifier.align(
                            if (it.from == Sender.Other) Alignment.CenterStart
                            else Alignment.CenterEnd
                        )
                        ChatMessage(it, position)
                    }

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
                },
                modifier = Modifier.fillMaxWidth(),
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
    modifier: Modifier = Modifier,
) {
    Surface(modifier, elevation = 4.dp) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            Arrangement.spacedBy(16.dp),
            Alignment.CenterVertically,
        ) {
            // TODO : Fix weird offset due to missing label.
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                onImeActionPerformed = { _, _ -> onSubmit() },
                label = { Text(stringResource(R.string.chat_message_placeholder)) },
                modifier = Modifier.weight(1f, fill = true),
            )
            Providers(AmbientContentAlpha provides ContentAlpha.medium) {
                IconButton(onSubmit) {
                    Icon(vectorResource(R.drawable.ic_chat_send))
                }
            }
        }
    }
}

@Preview
@Composable
private fun ChatPreview() {
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
        Chat(
            displayName = "Aloy",
            displayPicture = "https://pbs.twimg.com/profile_images/1257192502916001794/f1RW6Ogf_400x400.jpg",
            messages = messages,
            onBack = { },
            onSendMessageClick = { },
        )
    }
}
