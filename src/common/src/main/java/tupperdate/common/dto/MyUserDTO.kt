package tupperdate.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class MyUserDTO(
    val displayName: String,
    val imageBase64: String?,
    )
