package tupperdate.web.facade.recipes

import tupperdate.web.facade.PictureUrl

data class Recipe(
    val id: String,
    val title: String,
    val description: String,
    val picture: PictureUrl?,
    val hasAllergens: Boolean,
    val vegetarian: Boolean,
    val warm: Boolean,
    val timestamp: Long,
)
