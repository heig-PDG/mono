package tupperdate.common.model

import kotlinx.serialization.Serializable

@Serializable
data class NewUser(
    val displayName: String? = null,
)

@Serializable
data class User(
    val id: String? = null,
    val displayName: String? = null,
    val phone: String? = null,
    val picture: String? = null,
)
