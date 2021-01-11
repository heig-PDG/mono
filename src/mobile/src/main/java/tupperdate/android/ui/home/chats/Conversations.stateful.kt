package tupperdate.android.ui.home.chats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.getViewModel

@Composable
fun Conversations(
    modifier: Modifier = Modifier,
) {
    val viewModel = getViewModel<ConversationsViewModel>()
    val matches by viewModel.matches.collectAsState(emptyList())
    val conversations by viewModel.conversations.collectAsState(emptyList())

    Conversations(
        onRecipeClick = {},
        onProfileClick = {},
        recipes = emptyList(),
        conversations = conversations,
        modifier = modifier,
    )
}
