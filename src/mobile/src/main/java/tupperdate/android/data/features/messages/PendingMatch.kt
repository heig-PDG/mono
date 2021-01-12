package tupperdate.android.data.features.messages

/**
 * A [PendingMatch] is a [Match] that has not been accepted yet by the current user. Usually, it
 * should be displayed as a top-level non-dismissible dialog.
 *
 * Accepting a [PendingMatch] discards it, and creates an associated [Match].
 *
 * @see Match the non-pending variant of a [PendingMatch].
 */
data class PendingMatch(
    val identifier: ConversationIdentifier,
    val myPicture: String?,
    val theirPicture: String?,
)
