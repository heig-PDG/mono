package tupperdate.android.data.features.messages.store

import com.dropbox.android.external.store4.SourceOfTruth
import kotlinx.coroutines.flow.map
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.messages.Conversation
import tupperdate.android.data.features.messages.room.ConversationEntity
import tupperdate.android.data.features.messages.room.MessageDao
import tupperdate.common.dto.ConversationDTO

@InternalDataApi
class AllConversationSourceOfTruth(
    private val dao: MessageDao,
) : SourceOfTruth<Unit, List<ConversationDTO>, List<Conversation>> {

    override suspend fun delete(key: Unit) {
        dao.conversationDeleteAll()
    }

    override suspend fun deleteAll() {
        dao.conversationDeleteAll()
    }

    override fun reader(key: Unit) = dao.conversations().map {
        it.map { conv ->
            Conversation(
                identifier = conv.identifier,
                displayName = conv.name,
                picture = conv.picture ?: "", // TODO : Handle missing profile pictures.
                acknowledged = conv.accepted,
            )
        }
    }

    override suspend fun write(
        key: Unit,
        value: List<ConversationDTO>,
    ) {
        for (conv in value) {
            dao.conversationSave(
                ConversationEntity(
                    identifier = conv.userId,
                    name = conv.displayName,
                    picture = conv.picture,
                    previewBody = conv.lastMessage?.content,
                    previewTimestamp = conv.lastMessage?.timestamp,
                    accepted = false,
                )
            )
        }
    }
}
