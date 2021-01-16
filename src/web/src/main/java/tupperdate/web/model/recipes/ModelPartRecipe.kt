package tupperdate.web.model.recipes

import tupperdate.web.facade.PictureBase64
import tupperdate.web.facade.recipes.PartRecipe

data class ModelPartRecipe(
    val identifier: String,
    val title: String?,
    val titleProvided: Boolean,
    val description: String?,
    val descriptionProvided: Boolean,
    val picture: PictureBase64?,
    val pictureProvided: Boolean,
    val hasAllergens: Boolean,
    val hasAllergensProvided: Boolean,
    val vegetarian: Boolean,
    val vegetarianProvided: Boolean,
    val warm: Boolean,
    val warmProvided: Boolean,
)

fun PartRecipe.toModelPartRecipe(recipeId: String): ModelPartRecipe {
    return ModelPartRecipe(
        identifier = recipeId,
        title = title,
        titleProvided = titleProvided,
        description = description,
        descriptionProvided = descriptionProvided,
        picture = picture,
        pictureProvided = pictureProvided,
        hasAllergens = hasAllergens,
        hasAllergensProvided = hasAllergensProvided,
        vegetarian = vegetarian,
        vegetarianProvided = vegetarianProvided,
        warm = warm,
        warmProvided = warmProvided,
    )
}
