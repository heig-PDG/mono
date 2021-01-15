package tupperdate.android.ui.home.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tupperdate.android.data.features.messages.ConversationIdentifier
import tupperdate.android.data.features.messages.Message
import tupperdate.android.data.features.messages.MessagesRepository

class ChatViewModel(
    private val id: ConversationIdentifier,
    private val repository: MessagesRepository,
) : ViewModel() {

    val displayName: Flow<String> = repository.conversationInfo(id)
        .filterNotNull()
        .map { it.displayName }

    val displayPicture: Flow<String?> = repository.conversationInfo(id)
        .filterNotNull()
        .map { it.displayPictureUrl }

    val messages: Flow<List<Message>> = repository.messages(id)
        .map { it.sortedByDescending { msg -> msg.timestamp } }

    val recipesMine: Flow<List<String?>> = repository.conversation(id)
        .filterNotNull()
        .map { it.myRecipePictures }

    val recipesTheirs: Flow<List<String?>> = repository.conversation(id)
        .filterNotNull()
        .map { it.theirRecipePictures }

    fun onSend(message: String) {
        viewModelScope.launch {
            repository.send(id, message)
        }
    }
}
