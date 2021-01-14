package tupperdate.web.facade.recipes

data class NewRecipe<Picture>(
    val title: String,
    val description: String,
    val picture: Picture?,
    val hasAllergens: Boolean,
    val vegetarian: Boolean,
    val warm: Boolean,
)
