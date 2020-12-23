package tupperdate.android.data.features.recipe.room

import tupperdate.android.data.features.recipe.Recipe
import tupperdate.common.dto.NewRecipeDTO
import tupperdate.common.dto.RecipeAttributesDTO
import tupperdate.common.dto.RecipeDTO

/**
 * Transforms a [RecipeDTO] to a [Recipe], so it can be stored locally.
 */
fun RecipeDTO.asRecipeEntity(): RecipeEntity {
    return RecipeEntity(
        identifier = this.id,
        title = this.title,
        description = this.description,
        picture = this.picture,
        timestamp = this.timestamp,
    )
}

fun NewRecipeEntity.asDTO(): NewRecipeDTO {
    return NewRecipeDTO(
        title = this.title,
        description = this.description,
        attributes = RecipeAttributesDTO(
            vegetarian = this.isVegetarian,
            hasAllergens = this.hasAllergens,
            warm = this.isWarm,
        ),
        imageBase64 = this.picture,
    )
}
