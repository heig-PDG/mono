package tupperdate.android.data.features.messages

/**
 * A [Message] is received from other users, or sent to them.
 *
 * @param identifier the unique identifier for this message.
 * @param timestamp the timestamp at which this message was sent.
 * @param pending true if the message is currently in the sending queue.
 * @param body the [String] contents of this message.
 * @param from the sender of this message.
 */
data class Message(
    val identifier: String,
    val timestamp: Long,
    val pending: Boolean,
    val body: String,
    val from: Sender,
)

/**
 * A sender is either the currently logged in user, or another correspondent in a conversation.
 */
enum class Sender {
    Myself,
    Other,
}
