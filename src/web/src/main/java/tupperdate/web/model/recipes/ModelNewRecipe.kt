package tupperdate.web.model.recipes

import tupperdate.web.facade.PictureBase64
import tupperdate.web.facade.recipes.NewRecipe

data class ModelNewRecipe(
    val title: String,
    val description: String,
    val picture: PictureBase64?,
    val attributes: Map<String, Boolean>,
)

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
