package tupperdate.android.data.features.recipe.room

import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.common.dto.RecipeDTO

/**
 * Transforms a [RecipeDTO] to a [Recipe], so it can be stored locally.
 */
@InternalDataApi
fun RecipeDTO.asRecipeEntity(): RecipeEntity {
    return RecipeEntity(
        identifier = this.id,
        title = this.title,
        description = this.description,
        picture = this.picture,
        timestamp = this.timestamp,
    )
}
