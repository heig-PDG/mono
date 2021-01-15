package tupperdate.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class NewNotificationTokenDTO(
    val token: String,
)
