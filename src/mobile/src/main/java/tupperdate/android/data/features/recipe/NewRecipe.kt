package tupperdate.android.data.features.recipe

data class NewRecipe(
    val title: String,
    val description: String,
    val picture: String?, // base64 encoded.

    // Recipe attributes.
    val isVegan: Boolean,
    val isWarm: Boolean,
    val hasAllergens: Boolean,
)
