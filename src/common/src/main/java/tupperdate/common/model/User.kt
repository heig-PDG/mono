package tupperdate.common.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val displayName: String,
    val phone: String,
    val profilePictureUrl: String,
    val recipes: List<Recipe>,
)
