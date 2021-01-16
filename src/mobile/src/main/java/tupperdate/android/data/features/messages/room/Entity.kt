package tupperdate.android.data.features.messages.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.firebase.FirebaseUid

/**
 * A [ConversationEntity] stores the details of a conversation. This includes the preview message
 * body (if it actually exists), as well as some meta-data that indicates whether the conversation
 * was accepted on the device.
 *
 * @see ConversationRecipeEntity the recipes associated with a [ConversationEntity].
 */
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

/**
 * Each [ConversationEntity] comes with a set of [ConversationRecipeEntity]. These recipes are
 * associated directly with the conversation entity; if it gets deleted, the associated recipes will
 * get deleted too.
 *
 * These recipes can be displayed to the user to indicate what made multiple users match. This will
 * for instance be the case match bubbles, or at the start of a direct conversation between two
 * users.
 *
 * TODO : Actually store the recipes in the fetchers.
 */
@InternalDataApi
@Entity(
    tableName = "conversationsRecipes",
    primaryKeys = ["convId", "recipeId"],
    foreignKeys = [ForeignKey(
        entity = ConversationEntity::class,
        parentColumns = ["id"],
        childColumns = ["convId"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE,
    )]
)
data class ConversationRecipeEntity(
    @ColumnInfo(name = "convId") val convId: String,
    @ColumnInfo(name = "recipeId") val recipeId: String,
    @ColumnInfo(name = "recipeBelongsToThem") val recipeBelongsToThem: Boolean,
    @ColumnInfo(name = "recipePic") val recipePicture: String?,
    @ColumnInfo(name = "recipeName") val recipeName: String,
)

/**
 * A [MessageEntity] is exchanged between two participants. It has a sender, a recipient, a body,
 * was sent at a certain timestamp (issued by the server) and a unique identifier.
 *
 * @see PendingMessageEntity for creating new messages
 */
@InternalDataApi
@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val identifier: String,
    @ColumnInfo(name = "localId") val localIdentifier: String,
    @ColumnInfo(name = "uidFrom") val from: String,
    @ColumnInfo(name = "uidTo") val to: String,
    @ColumnInfo(name = "timestampMillis") val timestamp: Long,
    @ColumnInfo(name = "body") val body: String,
)

/**
 * Sent messages are created as [PendingMessageEntity], before being sent via a worker
 * asynchronously. Because messages are sent in the order of their [identifier], no reordering takes
 * place in case of intermittent connectivity with the remote server.
 */
@InternalDataApi
@Entity(tableName = "messagesCreations")
data class PendingMessageEntity(
    @PrimaryKey
    @ColumnInfo(name = "localId") val identifier: String,
    @ColumnInfo(name = "localTimestampMillis") val timestamp: Long,
    @ColumnInfo(name = "sent") val sent: Boolean,
    @ColumnInfo(name = "uid") val recipient: FirebaseUid,
    @ColumnInfo(name = "body") val body: String,
)
