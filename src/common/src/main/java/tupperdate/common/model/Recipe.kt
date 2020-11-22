package tupperdate.common.model

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: String? = null,
    val title: String? = null,
    val timestamp: String? = null,
    val added: Long? = null,
    val picture: String? = null,
    val attributes: Map<String, Boolean> = emptyMap()
)
