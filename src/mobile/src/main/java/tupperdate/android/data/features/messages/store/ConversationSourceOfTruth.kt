package tupperdate.android.data.features.messages.store

import com.dropbox.android.external.store4.SourceOfTruth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.firebase.FirebaseUid
import tupperdate.android.data.features.messages.Conversation
import tupperdate.android.data.features.messages.room.ConversationEntity
import tupperdate.android.data.features.messages.room.MessageDao
import tupperdate.common.dto.ConversationDTO

@InternalDataApi
class ConversationSourceOfTruth(
    private val dao: MessageDao,
) : SourceOfTruth<FirebaseUid, ConversationDTO, Conversation> {

    override suspend fun delete(key: FirebaseUid) {
        dao.conversationDelete(key)
    }

    override suspend fun deleteAll() {
        dao.conversationDeleteAll()
    }

    override fun reader(
        key: FirebaseUid,
    ): Flow<Conversation?> {
        return dao.conversation(key).map {
            it?.let { conv ->
                Conversation(
                    identifier = conv.identifier,
                    displayName = conv.name,
                    picture = conv.picture ?: "", // TODO : Handle missing profile pictures.
                    acknowledged = conv.accepted,
                )
            }
        }
    }

    override suspend fun write(
        key: FirebaseUid,
        value: ConversationDTO,
    ) {
        dao.conversationSave(
            ConversationEntity(
                identifier = value.userId,
                name = value.displayName,
                picture = value.picture,
                previewBody = value.lastMessage?.content,
                previewTimestamp = value.lastMessage?.timestamp,
                accepted = false,
            )
        )
    }
}
