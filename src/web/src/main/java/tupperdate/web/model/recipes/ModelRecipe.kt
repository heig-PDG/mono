package tupperdate.web.model.recipes

data class ModelRecipe<Picture>(
    val identifier: String,
    val title: String,
    val description: String,
    val picture: Picture?,
    val timestamp: Long,
    val attributes: Map<String, Boolean>,
)