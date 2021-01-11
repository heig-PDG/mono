package tupperdate.android.data.features.messages.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.firebase.FirebaseUid

@Dao
@InternalDataApi
abstract class MessageDao {

    @Query("SELECT * FROM conversations")
    abstract fun conversations(): Flow<List<ConversationEntity>>

    /**
     * Retrieves all the [ConversationEntity] that have not been accepted yet.
     */
    @Query(
        """
        SELECT * FROM conversations
        WHERE conversations.accepted = 0
        """
    )
    abstract fun conversationsNotAccepted(): Flow<List<ConversationEntity>>

    /**
     * Retrieves all the [ConversationEntity] that have already been accepted.
     */
    @Query(
        """
        SELECT * FROM conversations
        WHERE conversations.accepted != 0
        """
    )
    abstract fun conversationsAccepted(): Flow<List<ConversationEntity>>

    /**
     * Retrieves a specific conversation.
     */
    @Query("SELECT * FROM conversations WHERE conversations.id = :forUid")
    abstract fun conversation(forUid: FirebaseUid): Flow<ConversationEntity?>

    @Query("SELECT * FROM conversations WHERE conversations.id = :forUid")
    abstract suspend fun conversationOnce(forUid: FirebaseUid): ConversationEntity?

    @Query("DELETE FROM conversations WHERE conversations.id = :forUid")
    abstract suspend fun conversationDelete(forUid: FirebaseUid)

    @Query("DELETE FROM conversations")
    abstract suspend fun conversationDeleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun conversationReplace(conv: ConversationEntity)

    @Transaction
    open suspend fun conversationSave(conv: ConversationEntity) {
        val existing = conversationOnce(conv.identifier)
        if (existing != null) {
            conversationReplace(conv.copy(accepted = existing.accepted))
        } else {
            conversationReplace(conv.copy(accepted = false))
        }
    }

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

    /**
     * Accepts the conversation with the given id.
     */
    @Query("UPDATE conversations SET accepted = 1 WHERE conversations.id = :forUid")
    abstract suspend fun accept(forUid: String)

    /**
     * Posts a new [PendingMessageEntity], which will eventually get synced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun post(message: PendingMessageEntity)
}
