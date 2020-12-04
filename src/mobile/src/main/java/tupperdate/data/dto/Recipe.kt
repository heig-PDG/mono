package tupperdate.data.dto

import tupperdate.api.RecipeApi
import tupperdate.common.dto.RecipeDTO

fun RecipeDTO.asRecipe(): RecipeApi.Recipe {
    return RecipeApi.Recipe(
        title = this.title,
        description = this.description,
        pictureUrl = this.picture,
    )
}
