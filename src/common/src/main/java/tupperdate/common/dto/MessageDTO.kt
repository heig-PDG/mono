package tupperdate.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessageDTO(
    val senderId: String,
    val timestamp: String,
    val content: String,
)
