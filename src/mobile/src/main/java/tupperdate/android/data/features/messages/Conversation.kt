package tupperdate.android.data.features.messages

/**
 * A [Conversation] takes place between the currently logged in user, and a separate user. It has
 * necessarily been accepted, and contains a preview of some existing messages.
 *
 * @param identifier the unique identifier for this [Conversation].
 * @param picture the URL of the picture to display.
 * @param previewTitle the title to to display for the preview.
 * @param previewBody the body to display for the preview.
 * @param previewTimestamp the timestamp to use in order to eventually re-order the previews.
 * @param myRecipePictures the recipes that this user liked from me.
 * @param theirRecipePictures the recipes that I liked from this user.
 */
data class Conversation(
    val identifier: ConversationIdentifier,
    val picture: String?,
    val previewTitle: String,
    val previewBody: String,
    val previewTimestamp: Long,
    val myRecipePictures: List<String?>,
    val theirRecipePictures: List<String?>,
)
