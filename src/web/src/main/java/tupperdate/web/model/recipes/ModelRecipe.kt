package tupperdate.web.model.recipes

data class ModelRecipe<Picture>(
    val identifier: String,
    val title: String,
    val description: String,
    val picture: Picture?,
    val timestamp: Long,
    val hasAllergens: Boolean,
    val vegetarian: Boolean,
    val warm: Boolean,
)
