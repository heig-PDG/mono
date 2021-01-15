package tupperdate.web.model.recipes

import tupperdate.web.facade.PictureBase64
import tupperdate.web.facade.PictureUrl
import tupperdate.web.facade.recipes.NewRecipe
import tupperdate.web.model.recipes.firestore.FirestoreRecipe

data class ModelNewRecipe(
    val title: String,
    val description: String,
    val picture: PictureBase64?,
    val attributes: Map<String, Boolean>,
)

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

fun NewRecipe.toModelNewRecipe(): ModelNewRecipe {
    return ModelNewRecipe(
        title = this.title,
        description = this.description,
        picture = this.picture,
        attributes = mapOf(
            "hasAllergens" to this.hasAllergens,
            "vegetarian" to this.vegetarian,
            "warm" to this.warm,
        )
    )
}
