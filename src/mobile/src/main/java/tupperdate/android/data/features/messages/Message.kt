package tupperdate.android.data.features.messages

data class Message(
    val timestamp: Long,
    val body: String,
    val sent: Boolean,
    val from: Sender,
)

enum class Sender {
    Myself,
    Other,
}
