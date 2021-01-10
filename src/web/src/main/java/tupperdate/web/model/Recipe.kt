package tupperdate.web.model

import io.ktor.http.*
import tupperdate.common.dto.NewRecipeDTO
import tupperdate.common.dto.RecipeAttributesDTO
import tupperdate.common.dto.RecipeDTO
import tupperdate.web.exceptions.statusException

data class Recipe(
    val id: String? = null,
    val userId: String? = null,
    val title: String? = null,
    val description: String? = null,
    val timestamp: Long? = null,
    val picture: String? = null,
    val attributes: Map<String, Boolean>? = null,
)

fun NewRecipeDTO.toRecipe(id: String, userId: String, picture: String): Recipe {
    return Recipe(
        id = id,
        userId = userId,
        title = this.title,
        description = this.description,
        timestamp = System.currentTimeMillis() / 1000,
        picture = picture,
        attributes = mapOf(
            "hasAllergens" to this.attributes.hasAllergens,
            "vegetarian" to this.attributes.vegetarian,
            "warm" to this.attributes.warm,
        ),
    )
}

fun Recipe.toRecipeDTO(): RecipeDTO {
    return RecipeDTO(
        id = requireNotNull(this.id),
        title = this.title ?: "No title",
        description = this.description ?: "No description",
        timestamp = this.timestamp ?: 0,
        picture = this.picture ?: "https://www.specialtyproduce.com/sppics/691.png",
        attributes = RecipeAttributesDTO(
            hasAllergens = this.attributes?.get("hasAllergens") ?: statusException(HttpStatusCode.InternalServerError),
            vegetarian = this.attributes["vegetarian"] ?: statusException(HttpStatusCode.InternalServerError),
            warm = this.attributes["warm"] ?: statusException(HttpStatusCode.InternalServerError),
        )
    )
}
