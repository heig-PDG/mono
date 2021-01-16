package tupperdate.android.data.features.messages.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.firebase.FirebaseUid

/**
 * A [Dao] that is used to perform anything and everything related to managing messages.
 */
@InternalDataApi
@Dao
abstract class MessageDao {

    /**
     * Retrieves all the [MessageEntity] for a certain conversation.
     */
    @Query(
        """
        SELECT * FROM messages
        WHERE messages.uidFrom = :forUid
        OR messages.uidTo = :forUid
        """
    )
    abstract fun messages(forUid: FirebaseUid): Flow<List<MessageEntity>>

    @Query("DELETE FROM messages WHERE messages.id = :forId")
    abstract suspend fun messagesDelete(forId: String)

    @Query("DELETE FROM messages")
    abstract suspend fun messagesDeleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun messagesReplace(entity: MessageEntity)

    @Query("SELECT * FROM messages WHERE messages.localId = :forId")
    abstract suspend fun messageForLocalIdOnce(forId: String): MessageEntity?

    /**
     * Retrieves all the [PendingMessageEntity], which still need to be sent to the server.
     */
    @Query("SELECT * FROM messagesCreations WHERE messagesCreations.uid = :forUid")
    abstract fun pending(forUid: FirebaseUid): Flow<List<PendingMessageEntity>>

    @Query("SELECT * FROM messagesCreations WHERE messagesCreations.sent = 0")
    abstract fun pendingToSend(): Flow<List<PendingMessageEntity>>

    @Query("SELECT * FROM messagesCreations WHERE messagesCreations.sent != 0")
    abstract fun pendingSent(): Flow<List<PendingMessageEntity>>

    @Transaction
    open suspend fun pendingClear(forLocalId: String) {
        pendingOnce(forLocalId) ?: return
        messageForLocalIdOnce(forLocalId) ?: return
        pendingDelete(forLocalId)
    }

    /**
     * Deletes the [PendingMessageEntity] with the provided local id..
     */
    @Query("DELETE FROM messagesCreations WHERE messagesCreations.localId = :forLocalId")
    abstract fun pendingDelete(forLocalId: String)

    @Query("SELECT * FROM messagesCreations WHERE messagesCreations.localId = :forLocalId")
    abstract suspend fun pendingOnce(forLocalId: String): PendingMessageEntity?

    @Transaction
    open suspend fun pendingMarkSent(forLocalId: String) {
        val message = pendingOnce(forLocalId) ?: return
        post(message.copy(sent = true))
    }

    /**
     * Posts a new [PendingMessageEntity], which will eventually get synced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun post(message: PendingMessageEntity)
}
