package tupperdate.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecipeAttributesDTO(
    val hasAllergens: Boolean,
    val vegetarian: Boolean,
    val warm: Boolean,
)
