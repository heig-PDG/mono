package tupperdate.android.data.features.messages

/**
 * A [Match] is created between two users when a [Conversation] has not started (i.e. there is no
 * preview message to display) but some recipes have been liked in a reciprocal fashion.
 *
 * @param identifier the identifier for this [Match].
 * @param myPictures a [List] of all the match pictures I have.
 * @param theirPictures a [List] of all the match pictures they have.
 */
data class Match(
    val identifier: ConversationIdentifier,
    val myPictures: List<String?>,
    val theirPictures: List<String?>,
)
