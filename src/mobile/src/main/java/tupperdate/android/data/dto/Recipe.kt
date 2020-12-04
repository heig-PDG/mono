package tupperdate.android.data.dto

import tupperdate.android.data.api.RecipeApi
import tupperdate.common.dto.RecipeDTO

fun RecipeDTO.asRecipe(): RecipeApi.Recipe {
    return RecipeApi.Recipe(
        title = this.title,
        description = this.description,
        pictureUrl = this.picture,
    )
}
