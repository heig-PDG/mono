package tupperdate.android.data.features.messages.store

import com.dropbox.android.external.store4.SourceOfTruth
import kotlinx.coroutines.flow.Flow
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.firebase.FirebaseUid
import tupperdate.android.data.features.messages.room.ConversationEntity
import tupperdate.android.data.features.messages.room.ConversationDao
import tupperdate.common.dto.ConversationDTO

/**
 * A [SourceOfTruth] that stores individual [ConversationDTO] in some [ConversationEntity]. It is
 * based around the [ConversationDao].
 *
 * @see AllConversationSourceOfTruth a variant that persists a [List] of [ConversationEntity].
 */
@InternalDataApi
class OneConversationSourceOfTruth(
    private val dao: ConversationDao,
) : SourceOfTruth<FirebaseUid, ConversationDTO, ConversationEntity> {

    override suspend fun delete(key: FirebaseUid) {
        dao.conversationDelete(key)
    }

    override suspend fun deleteAll() {
        dao.conversationDeleteAll()
    }

    override fun reader(
        key: FirebaseUid,
    ): Flow<ConversationEntity?> {
        return dao.conversation(key)
    }

    override suspend fun write(
        key: FirebaseUid,
        value: ConversationDTO,
    ) {
        dao.conversationSave(value)
    }
}
