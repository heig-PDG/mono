package tupperdate.android.data.room.recipe

import tupperdate.android.data.Recipe
import tupperdate.common.dto.RecipeDTO

/**
 * Transforms a [RecipeDTO] to a [Recipe], so it can be stored locally.
 */
fun RecipeDTO.asRecipe(): Recipe {
    return Recipe(
        identifier = this.id,
        title = this.title,
        description = this.description,
        picture = this.picture,
        timestamp = this.timestamp,
    )
}
