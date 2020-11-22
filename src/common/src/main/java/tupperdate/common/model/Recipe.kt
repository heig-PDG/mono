package tupperdate.common.model

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val timestamp: Long? = null,
    val picture: String? = null,
    val attributes: RecipeAttributes? = null,
)

@Serializable
data class RecipeAttributes(
    val hasAllergens: Boolean? = null,
    val vegetarian: Boolean? = null,
    val warm: Boolean? = null,
)
