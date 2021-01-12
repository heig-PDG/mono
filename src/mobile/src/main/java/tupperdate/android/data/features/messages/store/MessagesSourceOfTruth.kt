package tupperdate.android.data.features.messages.store

import com.dropbox.android.external.store4.SourceOfTruth
import kotlinx.coroutines.flow.first
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.AuthenticationRepository
import tupperdate.android.data.features.messages.ConversationIdentifier
import tupperdate.android.data.features.messages.room.MessageDao
import tupperdate.android.data.features.messages.room.MessageEntity
import tupperdate.common.dto.MessageDTO

@InternalDataApi
class MessagesSourceOfTruth(
    private val auth: AuthenticationRepository,
    private val messages: MessageDao,
) : SourceOfTruth<ConversationIdentifier, List<MessageDTO>, List<MessageEntity>> {

    override suspend fun delete(
        key: ConversationIdentifier,
    ) = messages.messagesDelete(key)

    override suspend fun deleteAll() = messages.messagesDeleteAll()

    override fun reader(
        key: ConversationIdentifier,
    ) = messages.messages(key)

    override suspend fun write(
        key: ConversationIdentifier,
        value: List<MessageDTO>,
    ) {
        val self = auth.identifier.first().identifier
        for (message in value) {
            val (sender, recipient) =
                if (message.senderId == self) Pair(self, key)
                else Pair(key, self)
            val entity = MessageEntity(
                identifier = message.id,
                body = message.content,
                timestamp = message.timestamp,
                from = sender,
                to = recipient,
            )
            messages.messagesReplace(entity)
        }
    }
}
