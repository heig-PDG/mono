package tupperdate.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class NewRecipeDTO(
    val title: String,
    val description: String,
    val attributes: RecipeAttributesDTO,
    val imageBase64: String?,
)
