package tupperdate.web.legacy.model

import io.ktor.http.*
import tupperdate.common.dto.NewRecipeDTO
import tupperdate.common.dto.RecipeAttributesDTO
import tupperdate.common.dto.RecipeDTO
import tupperdate.web.utils.statusException

data class Recipe(
    val id: String? = null,
    val userId: String? = null,
    val title: String? = null,
    val description: String? = null,
    val timestamp: Long? = null,
    val picture: String? = null,
    val attributes: Map<String, Boolean>? = null,
)

fun Recipe.toRecipeDTO(): RecipeDTO {
    return RecipeDTO(
        id = requireNotNull(this.id),
        userId = this.userId ?: statusException(HttpStatusCode.InternalServerError),
        title = this.title ?: statusException(HttpStatusCode.InternalServerError),
        description = this.description ?: statusException(HttpStatusCode.InternalServerError),
        timestamp = this.timestamp ?: statusException(HttpStatusCode.InternalServerError),
        picture = this.picture,
        attributes = RecipeAttributesDTO(
            hasAllergens = this.attributes?.get("hasAllergens")
                ?: statusException(HttpStatusCode.InternalServerError),
            vegetarian = this.attributes["vegetarian"]
                ?: statusException(HttpStatusCode.InternalServerError),
            warm = this.attributes["warm"] ?: statusException(HttpStatusCode.InternalServerError),
        )
    )
}
