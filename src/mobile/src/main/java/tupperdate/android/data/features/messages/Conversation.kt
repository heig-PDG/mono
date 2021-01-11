package tupperdate.android.data.features.messages

data class Conversation(
    val identifier: String,
    val displayName: String,
    val picture: String,
    val acknowledged: Boolean,
)
