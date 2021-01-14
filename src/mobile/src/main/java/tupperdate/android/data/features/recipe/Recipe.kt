package tupperdate.android.data.features.recipe

import tupperdate.android.data.features.auth.firebase.FirebaseUid

data class Recipe(
    val identifier: String,
    val owner: FirebaseUid,
    val title: String,
    val description: String,
    val timestamp: Long,
    val picture: String?,
    val attributes: RecipeAttributes,
)

data class RecipeAttributes(
    val vegetarian: Boolean,
    val warm: Boolean,
    val hasAllergens: Boolean,
)
