package tupperdate.android.data.features.messages.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.firebase.FirebaseUid

@InternalDataApi
@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val identifier: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "picture") val picture: String?,
    @ColumnInfo(name = "messageBody") val previewBody: String?,
    @ColumnInfo(name = "messageTimestamp") val previewTimestamp: Long?,
    @ColumnInfo(name = "accepted") val accepted: Boolean,
)

// TODO : Store the recipes as well.

@InternalDataApi
@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val identifier: String,
    @ColumnInfo(name = "uidFrom") val from: String,
    @ColumnInfo(name = "uidTo") val to: String,
    @ColumnInfo(name = "timestampMillis") val timestamp: Long,
    @ColumnInfo(name = "body") val body: String,
)

// TODO : Display pending messages without flashing.
@InternalDataApi
@Entity(tableName = "messagesCreations")
data class PendingMessageEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "localId") val identifier: Long = 0,
    @ColumnInfo(name = "uid") val recipient: FirebaseUid,
    @ColumnInfo(name = "body") val body: String,
)
