package tupperdate.android.data.features.recipe.room

import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.common.dto.RecipeDTO

/**
 * Transforms a [RecipeDTO] to a [Recipe], so it can be stored locally.
 */
@InternalDataApi
fun RecipeDTO.asRecipeEntity(inStack: Boolean?): RecipeEntity {
    return RecipeEntity(
        identifier = this.id,
        title = this.title,
        description = this.description,
        picture = this.picture,
        timestamp = this.timestamp,
        inStack = inStack,
    )
}

/**
 * Transforms a [RecipeEntity] to a [Recipe], so it can be passed through the API layer.
 */
@InternalDataApi
fun RecipeEntity.toRecipe(): Recipe {
    return Recipe(
        identifier = this.identifier,
        title = this.title,
        description = this.description,
        timestamp = this.timestamp,
        picture = this.picture,
    )
}
