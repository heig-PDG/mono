package tupperdate.web.facade.recipes

data class Recipe<Picture>(
    val id: String,
    val title: String,
    val description: String,
    val picture: Picture?,
    val hasAllergens: Boolean,
    val vegetarian: Boolean,
    val warm: Boolean,
    val timestamp: Long,
)
