package tupperdate.common.model

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val foodPictureUrl: String = "",
    val attributes: Map<String, Boolean> = emptyMap()
)
