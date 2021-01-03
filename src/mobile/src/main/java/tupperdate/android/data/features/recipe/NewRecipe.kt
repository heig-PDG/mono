package tupperdate.android.data.features.recipe

import android.net.Uri

data class NewRecipe(
    val title: String,
    val description: String,
    val picture: Uri?,

    // Recipe attributes.
    val isVegan: Boolean,
    val isWarm: Boolean,
    val hasAllergens: Boolean,
)
