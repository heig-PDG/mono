package tupperdate.android.ui.home.chats

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import tupperdate.android.data.features.messages.Conversation
import tupperdate.android.data.features.messages.Match
import tupperdate.android.data.features.messages.MessagesRepository

class ConversationsViewModel(
    private val messages: MessagesRepository,
) : ViewModel() {

    val matches: Flow<List<Match>> = messages.matches
    val conversations: Flow<List<Conversation>> = messages.conversations
}
