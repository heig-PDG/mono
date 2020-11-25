package tupperdate.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class NotificationTokenDTO(
    val token: String,
)
