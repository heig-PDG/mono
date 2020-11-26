package tupperdate.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: String,
    val displayName: String?,
    val picture: String?,
)
