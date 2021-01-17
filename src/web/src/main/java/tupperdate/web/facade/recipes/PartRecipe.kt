package tupperdate.web.facade.recipes

import tupperdate.common.dto.RecipePartDTO
import tupperdate.web.facade.PictureBase64

data class PartRecipe(
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

fun RecipePartDTO.toPartRecipe(): PartRecipe {
    val titlePair = when(val opt = title) {
        is utils.OptionalProperty.Provided -> opt.value to true
        utils.OptionalProperty.NotProvided -> null to false
    }
    val descriptionPair = when(val opt = description) {
        is utils.OptionalProperty.Provided -> opt.value to true
        utils.OptionalProperty.NotProvided -> null to false
    }
    val picturePair = when(val opt = picture) {
        is utils.OptionalProperty.Provided -> opt.value to true
        utils.OptionalProperty.NotProvided -> null to false
    }
    val hasAllergensPair = when(val opt = attributes.hasAllergens) {
        is utils.OptionalProperty.Provided -> opt.value to true
        utils.OptionalProperty.NotProvided -> false to false
    }
    val vegetarianPair = when(val opt = attributes.vegetarian) {
        is utils.OptionalProperty.Provided -> opt.value to true
        utils.OptionalProperty.NotProvided -> false to false
    }
    val warmPair = when(val opt = attributes.warm) {
        is utils.OptionalProperty.Provided -> opt.value to true
        utils.OptionalProperty.NotProvided -> false to false
    }

    return PartRecipe(
        title = titlePair.first,
        titleProvided = titlePair.second,
        description = descriptionPair.first,
        descriptionProvided = descriptionPair.second,
        picture = picturePair.first?.let { PictureBase64(it) },
        pictureProvided = picturePair.second,
        hasAllergens = hasAllergensPair.first,
        hasAllergensProvided = hasAllergensPair.second,
        vegetarian = vegetarianPair.first,
        vegetarianProvided = vegetarianPair.second,
        warm = warmPair.first,
        warmProvided = warmPair.second,

    )
}
