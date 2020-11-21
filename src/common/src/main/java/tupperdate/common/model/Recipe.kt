package tupperdate.common.model

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val added: Long? = null,
    val foodPictureUrl: String? = null,
    val dislikes: List<String> = emptyList(),
    val attributes: Map<String, Boolean> = emptyMap()
)
