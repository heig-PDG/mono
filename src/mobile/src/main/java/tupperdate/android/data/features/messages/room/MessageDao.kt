package tupperdate.android.data.features.messages.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    /**
     * Posts a new [PendingMessageEntity], which will eventually get synced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun post(message: PendingMessageEntity)
}
