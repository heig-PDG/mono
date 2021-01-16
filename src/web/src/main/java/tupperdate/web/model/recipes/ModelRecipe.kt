package tupperdate.web.model.recipes

import tupperdate.web.facade.PictureUrl

data class ModelRecipe(
    val identifier: String,
    val userId: String,
    val title: String,
    val description: String,
    val picture: PictureUrl?,
    val hasAllergens: Boolean,
    val vegetarian: Boolean,
    val warm: Boolean,
    val timestamp: Long,
)

