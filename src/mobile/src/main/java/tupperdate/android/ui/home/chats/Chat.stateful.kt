package tupperdate.android.ui.home.chats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import tupperdate.android.data.features.messages.ConversationIdentifier
import tupperdate.android.ui.theme.PlaceholderProfileImage

@Composable
fun Chat(
    id: ConversationIdentifier,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = getViewModel<ChatViewModel> { parametersOf(id) }
    val title by viewModel.displayName.collectAsState("")
    val picture by viewModel.displayPicture.collectAsState(null)
    val messages by viewModel.messages.collectAsState(emptyList())
    val recipesMine by viewModel.recipesMine.collectAsState(emptyList())
    val recipesTheirs by viewModel.recipesTheirs.collectAsState(emptyList())
    Chat(
        displayName = title,
        displayPicture = picture ?: PlaceholderProfileImage,
        messages = messages,
        recipesMine = recipesMine,
        recipesTheirs = recipesTheirs,
        onBack = onBack,
        // TODO : Avoid duplicate sends
        // TODO : Sender feedback
        onSendMessageClick = viewModel::onSend,
        modifier = modifier,
    )
}
