package tupperdate.android.ui.home.chats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
    val matches by viewModel.matches.collectAsState(emptyList())
    val conversations by viewModel.conversations.collectAsState(emptyList())

    Conversations(
        matches = matches,
        conversations = conversations,
        onConversationClick = onConversationClick,
        modifier = modifier,
    )
}
