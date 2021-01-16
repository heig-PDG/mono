package tupperdate.web.facade.recipes

import tupperdate.common.dto.NewRecipeDTO
import tupperdate.web.facade.PictureBase64

data class NewRecipe(
    val title: String,
    val description: String,
    val picture: PictureBase64?,
    val hasAllergens: Boolean,
    val vegetarian: Boolean,
    val warm: Boolean,
)

fun NewRecipeDTO.toNewRecipe(): NewRecipe {
    return NewRecipe(
        title = title,
        description = description,
        picture = imageBase64?.let(::PictureBase64),
        hasAllergens = attributes.hasAllergens,
        vegetarian = attributes.vegetarian,
        warm = attributes.warm,
    )
}
