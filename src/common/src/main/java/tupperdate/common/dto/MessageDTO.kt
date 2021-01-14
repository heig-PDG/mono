package tupperdate.common.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import utils.TimestampSerializer

@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class MessageDTO(
    val id: String,
    val tempId: String,
    val senderId: String,
    @Serializable(with = TimestampSerializer::class)
    val timestamp: Long,
    val content: String,
)
