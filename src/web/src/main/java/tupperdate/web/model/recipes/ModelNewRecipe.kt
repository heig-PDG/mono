package tupperdate.web.model.recipes

data class ModelNewRecipe<Picture>(
    val title: String,
    val description: String,
    val picture: Picture?,
    val hasAllergens: Boolean,
    val vegetarian: Boolean,
    val warm: Boolean,
)
