package tupperdate.android.data.features.messages

/**
 * A [ConversationInfo] is some meta-data about a conversation with a user. Unlike a [Conversation]
 * which has had at least one message exchanged, any [Match], [PendingMatch] or [Conversation] has
 * a [ConversationInfo].
 *
 * @param displayName the name of this [ConversationInfo].
 * @param displayPictureUrl the URL to display, if provided.
 */
data class ConversationInfo(
    val displayName: String,
    val displayPictureUrl: String?,
)
