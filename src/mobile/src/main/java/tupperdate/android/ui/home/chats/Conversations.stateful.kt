package tupperdate.android.ui.home.chats

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import tupperdate.android.data.features.auth.firebase.FirebaseUid

/**
 * A stateful variant of the [Conversations] composable.
 *
 * @param onConversationClick a callback invoked whenever a conversation or a match is clicked.
 * @param modifier the modifier for this composable.
 */
@Composable
fun Conversations(
    onConversationClick: (FirebaseUid) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = getViewModel<ConversationsViewModel>()
    val matches = viewModel.matches.collectAsState(null).value
    val conversations = viewModel.conversations.collectAsState(null).value

    if (conversations != null && matches != null) {
        if (conversations.isEmpty() && matches.isEmpty()) {
            EmptyConversations(
                modifier = modifier
                    .fillMaxSize()
                    .padding(32.dp),
            )
        } else {
            Conversations(
                matches = matches,
                conversations = conversations,
                onConversationClick = onConversationClick,
                modifier = modifier,
            )
        }
    }
}
