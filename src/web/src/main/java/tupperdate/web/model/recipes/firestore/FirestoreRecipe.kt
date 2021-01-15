package tupperdate.web.model.recipes.firestore

import tupperdate.web.facade.PictureUrl
import tupperdate.web.model.recipes.ModelNewRecipe
import tupperdate.web.model.recipes.ModelRecipe

data class FirestoreRecipe(
    val id: String? = null,
    val userId: String? = null,
    val title: String? = null,
    val description: String? = null,
    val timestamp: Long? = null,
    val picture: String? = null,
    val attributes: Map<String, Boolean>? = null,
)


fun FirestoreRecipe.toModelRecipe(): ModelRecipe? {
    return ModelRecipe(
        identifier = id ?: return null,
        userId = userId ?: return null,
        title = title ?: return null,
        description = description ?: return null,
        picture = picture?.let(::PictureUrl),
        timestamp = timestamp ?: return null,
        hasAllergens = attributes?.get("hasAllergens") ?: return null,
        vegetarian = attributes.get("vegetarian") ?: return null,
        warm = attributes.get("warm") ?: return null,
    )
}

fun ModelNewRecipe.toFirestoreRecipe(id: String, userId: String, picture: PictureUrl?): FirestoreRecipe {
    return FirestoreRecipe(
        id = id,
        userId = userId,
        title = title,
        description = description,
        timestamp = System.currentTimeMillis(),
        picture = picture?.url,
        attributes = attributes,
    )
}
