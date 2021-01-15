package tupperdate.web.model.recipes.firestore

import io.ktor.http.*
import tupperdate.common.dto.NewRecipeDTO
import tupperdate.common.dto.RecipeAttributesDTO
import tupperdate.common.dto.RecipeDTO
import tupperdate.web.statusException

data class FirestoreRecipe(
    val id: String? = null,
    val userId: String? = null,
    val title: String? = null,
    val description: String? = null,
    val timestamp: Long? = null,
    val picture: String? = null,
    val attributes: Map<String, Boolean>? = null,
)

fun NewRecipeDTO.toFirestoreRecipe(id: String, userId: String, picture: String?): FirestoreRecipe {
    return FirestoreRecipe(
        id = id,
        userId = userId,
        title = this.title,
        description = this.description,
        timestamp = System.currentTimeMillis(),
        picture = picture,
        attributes = mapOf(
            "hasAllergens" to this.attributes.hasAllergens,
            "vegetarian" to this.attributes.vegetarian,
            "warm" to this.attributes.warm,
        ),
    )
}

fun FirestoreRecipe.toRecipeDTO(): RecipeDTO {
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
