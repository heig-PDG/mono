package tupperdate.web.model.recipes

import tupperdate.web.facade.PictureUrl
import tupperdate.web.facade.recipes.Recipe

data class ModelRecipe(
    val identifier: String,
    val userId: String,
    val title: String,
    val description: String,
    val picture: PictureUrl?,
    val hasAllergens: Boolean,
    val vegetarian: Boolean,
    val warm: Boolean,
    val timestamp: Long,
)

fun ModelRecipe.toRecipe(): Recipe {
    return Recipe(
        id = identifier,
        userId = userId,
        title = title,
        description = description,
        picture = picture,
        hasAllergens = hasAllergens,
        vegetarian = vegetarian,
        warm = warm,
        timestamp = timestamp,
    )
}
