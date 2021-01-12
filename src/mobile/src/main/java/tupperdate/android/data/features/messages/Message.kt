package tupperdate.android.data.features.messages

// TODO : Document this class.
data class Message(
    val identifier: String,
    val timestamp: Long,
    val body: String,
    val sent: Boolean,
    val from: Sender,
)

// TODO : Document this class.
enum class Sender {
    Myself,
    Other,
}
