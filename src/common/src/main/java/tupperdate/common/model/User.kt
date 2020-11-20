package tupperdate.common.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String? = null,
    val displayName: String? = null,
    val phone: String? = null,
    val profilePictureUrl: String? = null,
    val recipes: List<Recipe>? = null,
)
