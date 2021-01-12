package tupperdate.android.ui.home.chats

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tupperdate.android.data.features.messages.Conversation
import tupperdate.android.data.features.messages.Match
import tupperdate.android.data.features.messages.MessagesRepository

/**
 * The [ViewModel] in charge of displaying the list of conversations and matches.
 */
class ConversationsViewModel(
    messages: MessagesRepository,
) : ViewModel() {

    val matches: Flow<List<Match>> = messages.matches
    val conversations: Flow<List<Conversation>> = messages.conversations
        .map { it.sortedByDescending { conv -> conv.previewTimestamp } }
}
