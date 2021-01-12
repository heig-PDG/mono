package tupperdate.android.data.features.messages.store

import com.dropbox.android.external.store4.SourceOfTruth
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.messages.room.ConversationEntity
import tupperdate.android.data.features.messages.room.ConversationDao
import tupperdate.common.dto.ConversationDTO

/**
 * A [SourceOfTruth] that stores all the [ConversationDTO] in some [ConversationEntity] list. It is
 * based around the [ConversationDao].
 *
 * @see OneConversationSourceOfTruth a variant that persists a [List] of [ConversationEntity].
 */
@InternalDataApi
class AllConversationSourceOfTruth(
    private val dao: ConversationDao,
) : SourceOfTruth<Unit, List<ConversationDTO>, List<ConversationEntity>> {

    override suspend fun delete(key: Unit) {
        dao.conversationDeleteAll()
    }

    override suspend fun deleteAll() {
        dao.conversationDeleteAll()
    }

    override fun reader(key: Unit) = dao.conversations()

    override suspend fun write(key: Unit, value: List<ConversationDTO>) {
        for (conv in value) {
            dao.conversationSave(conv)
        }
    }
}
