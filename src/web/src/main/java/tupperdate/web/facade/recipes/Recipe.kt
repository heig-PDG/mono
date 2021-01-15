package tupperdate.web.facade.recipes

import tupperdate.common.dto.RecipeAttributesDTO
import tupperdate.common.dto.RecipeDTO
import tupperdate.web.facade.PictureUrl
import tupperdate.web.model.recipes.ModelRecipe

data class Recipe(
    val id: String,
    val userId: String,
    val title: String,
    val description: String,
    val picture: PictureUrl?,
    val hasAllergens: Boolean,
    val vegetarian: Boolean,
    val warm: Boolean,
    val timestamp: Long,
)

fun Recipe.toRecipeDTO(): RecipeDTO {
    return RecipeDTO(
        id = id,
        userId = userId,
        title = title,
        description = description,
        picture = picture?.url,
        timestamp = timestamp,
        attributes = RecipeAttributesDTO(
            hasAllergens = hasAllergens,
            vegetarian = vegetarian,
            warm = warm,
        )
    )
}

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
